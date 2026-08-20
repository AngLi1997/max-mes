package com.bmos.lims2.server.inspect.receive.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.lims2.common.constants.InspectItemConstants;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.inspect.receive.dto.RetentionReceivePageQueryDTO;
import com.bmos.lims2.server.inspect.sample.dto.SampleCollectionListDTO;
import com.bmos.lims2.server.inspect.sample.service.SampleService;
import com.bmos.lims2.server.inspect.receive.service.SampleReceiveService;
import com.bmos.lims2.server.task.dto.AssignableUserDTO;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description: 样品接收Service实现类
 * @Author: yigaohui
 * @Date: 2025/01/29 16:30
 */
@Service
@Slf4j
public class SampleReceiveServiceImpl implements SampleReceiveService {

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private com.bmos.lims2.server.inspect.retention.service.RetentionObservationService retentionObservationService;

    @Autowired
    private com.bmos.lims2.server.inspect.retention.mapper.RetentionReceiveLedgerMapper retentionReceiveLedgerMapper;

    @Autowired
    private com.bmos.lims2.server.material.mapper.MaterialMapper materialMapper;

    @Autowired
    private com.bmos.unit.service.UnitCache unitCache;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchReceiveSamples(List<Long> sampleIds) {
        if (CollectionUtils.isEmpty(sampleIds)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_EMPTY, "样品ID列表");
        }

        String receiverName = SysUserHolder.getUser().getUserName();
        log.info("批量接收样品，样品数量：{}，接收人：{}", sampleIds.size(), receiverName);

        LocalDateTime receiveTime = LocalDateTime.now();

        // 查询样品信息并验证
        List<Sample> samples = sampleMapper.selectBatchIds(sampleIds);
        if (samples.size() != sampleIds.size()) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        // 验证样品状态
        for (Sample sample : samples) {
            if (!sample.getSampled()) {
                throw new BmosException(LimsResponseCode.RECEIVE_ERROR_SAMPLE_NOT_SAMPLED,
                        sample.getSampleNo());
            }
            if (sample.getReceived()) {
                throw new BmosException(LimsResponseCode.RECEIVE_ERROR_SAMPLE_ALREADY_RECEIVED,
                        sample.getSampleNo());
            }
        }

        // 批量更新样品接收状态
        for (Sample sample : samples) {
            sampleService.markSampleAsReceived(sample.getId(), receiveTime);
        }

        log.info("批量接收样品完成，成功接收样品数量：{}", samples.size());
    }

    @Override
    public List<String> listReceiversByInspectionOrders(List<Long> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_EMPTY, "请验单ID列表");
        }

        // 逐个请验单处理：先取该单对应所有班组的人员交集，再在不同请验单之间取交集
        List<InspectionOrder> orders = inspectionOrderMapper.selectBatchIds(orderIds);
        if (CollectionUtils.isEmpty(orders) || orders.size() != orderIds.size()) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "部分请验单不存在");
        }

        java.util.LinkedHashSet<String> finalIntersection = null;

        for (InspectionOrder order : orders) {
            Long schemeVersionId = order.getSchemeVersionId();
            if (schemeVersionId == null) {
                return new ArrayList<>();
            }
            // 获取该请验单关联方案版本下的班组列表
            List<Long> teamIdsOfOrder = taskMapper.selectTeamIdsBySchemeVersionIds(java.util.Collections.singletonList(schemeVersionId));
            if (CollectionUtils.isEmpty(teamIdsOfOrder)) {
                return new ArrayList<>();
            }
            // 查询这些班组的用户，按班组分组
            List<AssignableUserDTO> usersOfOrderTeams = taskMapper.selectUsersByTeamIds(teamIdsOfOrder);
            if (CollectionUtils.isEmpty(usersOfOrderTeams)) {
                return new ArrayList<>();
            }
            java.util.Map<Long, java.util.LinkedHashSet<String>> teamToUsers = new java.util.LinkedHashMap<>();
            for (AssignableUserDTO u : usersOfOrderTeams) {
                if (u.getTeamId() == null) {
                    continue;
                }
                teamToUsers
                    .computeIfAbsent(u.getTeamId(), k -> new java.util.LinkedHashSet<>())
                    .add(u.getUserId());
            }

            // 计算该请验单内部“班组人员交集”
            java.util.LinkedHashSet<String> orderIntersection = null;
            for (Long teamId : teamIdsOfOrder) {
                java.util.Set<String> userIdsOfTeam = teamToUsers.get(teamId);
                if (userIdsOfTeam == null || userIdsOfTeam.isEmpty()) {
                    return new ArrayList<>();
                }
                if (orderIntersection == null) {
                    orderIntersection = new java.util.LinkedHashSet<>(userIdsOfTeam);
                } else {
                    orderIntersection.retainAll(userIdsOfTeam);
                    if (orderIntersection.isEmpty()) {
                        return new ArrayList<>();
                    }
                }
            }
            if (orderIntersection == null || orderIntersection.isEmpty()) {
                return new ArrayList<>();
            }

            // 与前面请验单的结果做交集
            if (finalIntersection == null) {
                finalIntersection = new java.util.LinkedHashSet<>(orderIntersection);
            } else {
                finalIntersection.retainAll(orderIntersection);
                if (finalIntersection.isEmpty()) {
                    return new ArrayList<>();
                }
            }
        }

        if (finalIntersection == null || finalIntersection.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(finalIntersection);
    }

    @Override
    public List<String> listReceiversBySamples(List<Long> sampleIds) {
        if (CollectionUtils.isEmpty(sampleIds)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_EMPTY, "样品ID列表");
        }
        List<Sample> samples = sampleMapper.selectBatchIds(sampleIds);
        if (CollectionUtils.isEmpty(samples) || samples.size() != sampleIds.size()) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "部分样品不存在");
        }
        // 提取样品对应的请验单ID并去重
        Set<Long> orderIdSet = new LinkedHashSet<>();
        for (Sample sample : samples) {
            if (sample.getInspectionOrderId() != null) {
                orderIdSet.add(sample.getInspectionOrderId());
            }
        }
        if (orderIdSet.isEmpty()) {
            return new ArrayList<>();
        }
        return listReceiversByInspectionOrders(new ArrayList<>(orderIdSet));
    }

    @Override
    public CommonPage<SampleCollectionListDTO> getRetentionReceivePageList(RetentionReceivePageQueryDTO queryDTO) {
        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询留样接收列表
        List<SampleCollectionListDTO> list = sampleMapper.selectRetentionReceivePageList(queryDTO);

        // 填充单位名称（从缓存获取）
        if (!CollectionUtils.isEmpty(list)) {
            for (SampleCollectionListDTO item : list) {
                if (item.getUnitId() != null) {
                    item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                }
            }
        }

        // 返回分页数据
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchReceiveRetentionSamples(List<Long> sampleIds, String storageLocation) {
        if (CollectionUtils.isEmpty(sampleIds)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_EMPTY, "样品ID列表");
        }
        if (storageLocation == null || storageLocation.trim().isEmpty()) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_EMPTY, "储存位置");
        }

        String receiverName = SysUserHolder.getUser().getUserName();
        String receiverId = SysUserHolder.getUser().getUserId();
        log.info("批量接收留样样品，样品数量：{}，接收人：{}，储存位置：{}", sampleIds.size(), receiverName, storageLocation);

        LocalDateTime receiveTime = LocalDateTime.now();

        // 查询样品信息并验证
        List<Sample> samples = sampleMapper.selectBatchIds(sampleIds);
        if (samples.size() != sampleIds.size()) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "部分样品不存在");
        }

        // 验证样品状态
        for (Sample sample : samples) {
            // 验证是否已取样
            if (!sample.getSampled()) {
                throw new BmosException(LimsResponseCode.RECEIVE_ERROR_SAMPLE_NOT_SAMPLED,
                        sample.getSampleNo());
            }
            // 验证是否已接收
            if (sample.getReceived()) {
                throw new BmosException(LimsResponseCode.RECEIVE_ERROR_SAMPLE_ALREADY_RECEIVED,
                        sample.getSampleNo());
            }
            // 验证是否作废
            if (sample.getDiscarded()) {
                throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品" + sample.getSampleNo() + "已作废");
            }
            // 验证是否是留样检验项目
            if (sample.getInspectItemId() == null || !sample.getInspectItemId().equals(InspectItemConstants.RETENTION_INSPECT_ITEM_ID)) {
                throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品" + sample.getSampleNo() + "不是留样样品");
            }
        }

        // 批量更新样品接收状态和储存位置
        for (Sample sample : samples) {
            sample.setReceived(true);
            sample.setReceiverId(receiverId);
            sample.setReceiverName(receiverName);
            sample.setReceiveTime(receiveTime);
            sample.setStorageLocation(storageLocation);
            sampleMapper.updateById(sample);
        }

        // 为每个留样样品生成观察任务
        for (Sample sample : samples) {
            try {
                retentionObservationService.generateObservationTasks(sample.getId());
            } catch (Exception e) {
                log.error("为样品{}生成观察任务失败：{}", sample.getSampleNo(), e.getMessage(), e);
                // 不影响主流程，记录日志后继续
            }
        }

        // 记录留样接收台账
        for (Sample sample : samples) {
            try {
                // 查询检验单获取批号
                InspectionOrder order = inspectionOrderMapper.selectById(sample.getInspectionOrderId());
                // 查询物料信息
                com.bmos.lims2.server.material.entity.Material material = null;
                if (order != null && order.getMaterialId() != null) {
                    material = materialMapper.selectById(order.getMaterialId());
                }

                // 创建接收台账记录
                com.bmos.lims2.server.inspect.retention.entity.RetentionReceiveLedger ledger =
                    new com.bmos.lims2.server.inspect.retention.entity.RetentionReceiveLedger();
                ledger.setSampleId(sample.getId());
                ledger.setSampleNo(sample.getSampleNo());
                if (order != null) {
                    ledger.setBatchNo(order.getBatchNo());
                }
                if (material != null) {
                    ledger.setMaterialId(material.getId());
                    ledger.setMaterialName(material.getName());
                    ledger.setMaterialCode(material.getCode());
                    ledger.setMaterialSpec(material.getSpecification());
                }
                ledger.setQuantity(sample.getQuantity());
                ledger.setUnitId(sample.getUnitId());
                ledger.setSamplerId(sample.getSamplerId());
                ledger.setSamplerName(sample.getSamplerName());
                ledger.setSamplingTime(sample.getSamplingTime());
                ledger.setReceiverId(receiverId);
                ledger.setReceiverName(receiverName);
                ledger.setReceiveTime(receiveTime);
                ledger.setStorageLocation(storageLocation);

                retentionReceiveLedgerMapper.insert(ledger);
            } catch (Exception e) {
                log.error("记录样品{}接收台账失败：{}", sample.getSampleNo(), e.getMessage(), e);
                // 不影响主流程，记录日志后继续
            }
        }

        log.info("批量接收留样样品完成，成功接收样品数量：{}", samples.size());
    }

    @Override
    public SampleCollectionListDTO scanRetentionSample(String sampleNo) {
        if (sampleNo == null || sampleNo.trim().isEmpty()) {
            throw new BmosException(LimsResponseCode.INVALID_PARAMETER_EMPTY, "样品编号");
        }

        log.info("扫描留样样品二维码，样品编号：{}", sampleNo);

        // 查询样品信息
        Sample sample = sampleMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Sample>()
                .eq(Sample::getSampleNo, sampleNo)
                .eq(Sample::getDeleted, false)
        );

        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "样品不存在，样品编号：" + sampleNo);
        }

        // 校验是否为留样样品
        if (sample.getInspectItemId() == null ||
            !sample.getInspectItemId().equals(InspectItemConstants.RETENTION_INSPECT_ITEM_ID)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品" + sampleNo + "不是留样样品");
        }

        // 校验样品是否已接收
        if (sample.getReceived() != null && sample.getReceived()) {
            throw new BmosException(LimsResponseCode.RECEIVE_ERROR_SAMPLE_ALREADY_RECEIVED, sampleNo);
        }

        // 查询检验单获取批号和物料信息
        InspectionOrder order = inspectionOrderMapper.selectById(sample.getInspectionOrderId());
        com.bmos.lims2.server.material.entity.Material material = null;
        if (order != null && order.getMaterialId() != null) {
            material = materialMapper.selectById(order.getMaterialId());
        }

        // 组装返回数据
        SampleCollectionListDTO dto = new SampleCollectionListDTO();
        dto.setId(sample.getId());
        dto.setSampleNo(sample.getSampleNo());
        dto.setSampleName(sample.getSampleName());
        dto.setInspectionOrderId(sample.getInspectionOrderId());
        dto.setInspectItemId(sample.getInspectItemId());
        dto.setQuantity(sample.getQuantity());
        dto.setUnitId(sample.getUnitId());
        dto.setSamplerId(sample.getSamplerId());
        dto.setSamplingTime(sample.getSamplingTime());
        dto.setReceiverId(sample.getReceiverId());
        dto.setReceiveTime(sample.getReceiveTime());
        dto.setCreateTime(sample.getCreateTime());
        dto.setTagPrinted(sample.getTagPrinted());

        // 设置检验单信息
        if (order != null) {
            dto.setOrderNo(order.getOrderNo());
            dto.setBatchNo(order.getBatchNo());
            dto.setRequestTime(order.getCreateTime());
            dto.setRequestUserId(order.getCreateBy());
        }

        // 设置物料信息
        if (material != null) {
            dto.setMaterialName(material.getName());
            dto.setMaterialCode(material.getCode());
            dto.setMaterialSpec(material.getSpecification());
        }

        // 填充单位名称（从缓存获取）
        if (dto.getUnitId() != null) {
            dto.setUnitName(unitCache.getGlobalUnitName(dto.getUnitId()));
        }

        return dto;
    }
}
