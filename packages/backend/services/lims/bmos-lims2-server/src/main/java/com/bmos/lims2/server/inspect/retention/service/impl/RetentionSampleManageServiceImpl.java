package com.bmos.lims2.server.inspect.retention.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.lims2.common.constants.InspectItemConstants;
import com.bmos.lims2.common.enums.AuditBusinessModule;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.audit.operationlog.entity.AuditOperationLogEntity;
import com.bmos.lims2.server.audit.operationlog.mapper.AuditOperationLogMapper;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.entity.Sample;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.mapper.SampleMapper;
import com.bmos.lims2.server.inspect.retention.dto.RetentionSampleManageListDTO;
import com.bmos.lims2.server.inspect.retention.dto.RetentionSampleManagePageQueryDTO;
import com.bmos.lims2.server.inspect.retention.entity.SampleCollectionLedger;
import com.bmos.lims2.server.inspect.retention.mapper.SampleCollectionLedgerMapper;
import com.bmos.lims2.server.inspect.retention.service.RetentionSampleManageService;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 留样样品管理Service实现类
 * @Author: yigaohui
 * @Date: 2026/02/09
 */
@Service
@Slf4j
public class RetentionSampleManageServiceImpl implements RetentionSampleManageService {

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private SampleCollectionLedgerMapper sampleCollectionLedgerMapper;

    @Autowired
    private AuditOperationLogMapper auditOperationLogMapper;

    @Autowired
    private com.bmos.unit.service.UnitCache unitCache;

    @Autowired
    private com.bmos.lims2.server.inspect.retention.service.RetentionObservationService retentionObservationService;

    @Autowired
    private com.bmos.lims2.server.inspect.retention.mapper.RetentionDestructionLedgerMapper retentionDestructionLedgerMapper;

    @Autowired
    private com.bmos.lims2.server.material.mapper.MaterialMapper materialMapper;

    @Override
    public CommonPage<RetentionSampleManageListDTO> getManagePageList(RetentionSampleManagePageQueryDTO queryDTO) {
        // 设置分页参数
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 查询留样样品管理列表
        List<RetentionSampleManageListDTO> list = sampleMapper.selectRetentionSampleManagePageList(queryDTO);

        // 使用全局缓存填充单位名称
        if (cn.hutool.core.collection.CollectionUtil.isNotEmpty(list)) {
            list.forEach(item -> {
                if (item.getUnitId() != null) {
                    item.setUnitName(unitCache.getGlobalUnitName(item.getUnitId()));
                }
            });
        }

        // 返回分页数据
        return CommonPage.convertPage(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void extendRetention(Long sampleId, LocalDate newExpiryDate) {
        // 查询样品信息
        Sample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        // 验证是否是留样样品
        if (sample.getInspectItemId() == null ||
            !sample.getInspectItemId().equals(InspectItemConstants.RETENTION_INSPECT_ITEM_ID)) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_NOT_RETENTION);
        }

        // 验证状态：已接收且未销毁
        if (!sample.getReceived()) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_EXTEND);
        }
        if (sample.getDestroyed()) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_DESTROYED_CANNOT_EXTEND);
        }

        // 验证新期限必须大于原期限
        if (sample.getRetentionExpiryDate() != null &&
            !newExpiryDate.isAfter(sample.getRetentionExpiryDate())) {
            throw new BmosException(LimsResponseCode.RETENTION_EXPIRY_MUST_AFTER_OLD);
        }

        LocalDate oldExpiryDate = sample.getRetentionExpiryDate();

        // 更新留样期限
        sample.setRetentionExpiryDate(newExpiryDate);
        sampleMapper.updateById(sample);

        // 记录操作历史
        AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
            .businessId(sampleId)
            .module(AuditBusinessModule.RETENTION_SAMPLE_MANAGE.name())
            .operationType(OperationType.RETENTION_EXTEND.getValue())
            .detail("{\"expireDateUpdate\":\"" + newExpiryDate + "\"}")
            .remark("")
            .build();
        auditOperationLogMapper.insert(logEntity);

        // 判断是否需要生成新的观察任务
        try {
            retentionObservationService.generateAdditionalTasksForExtension(sampleId, oldExpiryDate, newExpiryDate);
        } catch (Exception e) {
            log.error("为样品{}生成额外观察任务失败：{}", sampleId, e.getMessage(), e);
        }

        log.info("留样样品延期成功，样品ID：{}，原期限：{}，新期限：{}",
            sampleId, oldExpiryDate, newExpiryDate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectSample(Long sampleId, String collectQuantity, Long unitId,
                             String collectReason, String collectorId, String collectorName) {
        // 查询样品信息
        Sample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        // 验证是否是留样样品
        if (sample.getInspectItemId() == null ||
            !sample.getInspectItemId().equals(InspectItemConstants.RETENTION_INSPECT_ITEM_ID)) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_NOT_RETENTION);
        }

        // 验证状态：已接收且未销毁
        if (!sample.getReceived()) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_COLLECT);
        }
        if (sample.getDestroyed()) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_DESTROYED_CANNOT_COLLECT);
        }

        // 验证领用数量不能大于当前数量
        BigDecimal currentQty = new BigDecimal(sample.getCurrentQuantity());
        BigDecimal collectQty = new BigDecimal(collectQuantity);
        if (collectQty.compareTo(currentQty) > 0) {
            throw new BmosException(LimsResponseCode.RETENTION_COLLECT_QUANTITY_EXCEED);
        }

        // 更新当前数量
        BigDecimal newCurrentQty = currentQty.subtract(collectQty);
        sample.setCurrentQuantity(newCurrentQty.toString());
        sampleMapper.updateById(sample);

        // 查询检验单信息（用于记录台账）
        InspectionOrder order = inspectionOrderMapper.selectById(sample.getInspectionOrderId());

        // 查询物料信息
        com.bmos.lims2.server.material.entity.Material material = null;
        if (order != null && order.getMaterialId() != null) {
            material = materialMapper.selectById(order.getMaterialId());
        }

        // 记录领用台账
        SampleCollectionLedger ledger = new SampleCollectionLedger();
        ledger.setSampleId(sample.getId());
        ledger.setSampleNo(sample.getSampleNo());
        ledger.setBatchNo(order != null ? order.getBatchNo() : null);
        if (order != null) {
            ledger.setMaterialId(order.getMaterialId());
        }
        if (material != null) {
            ledger.setMaterialName(material.getName());
            ledger.setMaterialCode(material.getCode());
            ledger.setMaterialSpec(material.getSpecification());
        }
        ledger.setCollectQuantity(collectQuantity);
        ledger.setUnitId(unitId);
        ledger.setCollectReason(collectReason);
        ledger.setCollectorId(collectorId);
        ledger.setCollectorName(collectorName);
        ledger.setCollectTime(LocalDateTime.now());
        sampleCollectionLedgerMapper.insert(ledger);

        // 记录操作历史
        AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
            .businessId(sampleId)
            .module(AuditBusinessModule.RETENTION_SAMPLE_MANAGE.name())
            .operationType(OperationType.RETENTION_COLLECT.getValue())
            .detail("")
            .remark("")
            .build();
        auditOperationLogMapper.insert(logEntity);

        log.info("留样样品领用成功，样品ID：{}，领用数量：{}，剩余数量：{}",
            sampleId, collectQuantity, newCurrentQty);
    }

    @Override
    public List<com.bmos.lims2.server.audit.operationlog.vo.ListLogVO> getOperationHistory(Long sampleId) {
        // 查询该样品的所有操作历史
        return auditOperationLogMapper.listLogByBusinessIdAndModule(
            sampleId,
            AuditBusinessModule.RETENTION_SAMPLE_MANAGE.name()
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void destroySample(com.bmos.lims2.server.inspect.retention.dto.SampleDestructionDTO destructionDTO) {
        if (destructionDTO == null || destructionDTO.getSampleId() == null) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_ID_REQUIRED);
        }

        Long sampleId = destructionDTO.getSampleId();

        // 查询样品信息
        Sample sample = sampleMapper.selectById(sampleId);
        if (sample == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS);
        }

        // 验证是否是留样样品
        if (sample.getInspectItemId() == null ||
            !sample.getInspectItemId().equals(InspectItemConstants.RETENTION_INSPECT_ITEM_ID)) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_NOT_RETENTION);
        }

        // 验证状态：已接收且未销毁
        if (!sample.getReceived()) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_NOT_RECEIVED_CANNOT_DESTROY);
        }
        if (sample.getDestroyed()) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_ALREADY_DESTROYED);
        }

        // 更新样品状态为已销毁，并将数量清零
        sample.setDestroyed(true);
        sample.setCurrentQuantity("0");
        sampleMapper.updateById(sample);

        // 构建操作历史详情
        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append("{");
        if (destructionDTO.getDestructionReason() != null) {
            detailBuilder.append("\"destructionReason\":\"").append(destructionDTO.getDestructionReason()).append("\",");
        }
        if (destructionDTO.getDestructionMethod() != null) {
            detailBuilder.append("\"destructionMethod\":\"").append(destructionDTO.getDestructionMethod()).append("\",");
        }
        if (destructionDTO.getDestructionTime() != null) {
            detailBuilder.append("\"destructionTime\":\"").append(destructionDTO.getDestructionTime()).append("\",");
        }
        if (destructionDTO.getDestructionLocation() != null) {
            detailBuilder.append("\"destructionLocation\":\"").append(destructionDTO.getDestructionLocation()).append("\",");
        }
        if (destructionDTO.getDestructorName() != null) {
            detailBuilder.append("\"destructorName\":\"").append(destructionDTO.getDestructorName()).append("\",");
        }
        if (destructionDTO.getSupervisorName() != null) {
            detailBuilder.append("\"supervisorName\":\"").append(destructionDTO.getSupervisorName()).append("\",");
        }
        // 移除最后一个逗号
        if (detailBuilder.length() > 1 && detailBuilder.charAt(detailBuilder.length() - 1) == ',') {
            detailBuilder.setLength(detailBuilder.length() - 1);
        }
        detailBuilder.append("}");

        // 记录操作历史，备注使用用户输入的备注
        AuditOperationLogEntity logEntity = AuditOperationLogEntity.builder()
            .businessId(sampleId)
            .module(AuditBusinessModule.RETENTION_SAMPLE_MANAGE.name())
            .operationType(OperationType.RETENTION_DESTROY.getValue())
            .detail(detailBuilder.toString())
            .remark(destructionDTO.getRemark() != null ? destructionDTO.getRemark() : "")
            .build();
        auditOperationLogMapper.insert(logEntity);

        // 记录留样销毁台账
        try {
            // 查询检验单获取批号
            InspectionOrder order = inspectionOrderMapper.selectById(sample.getInspectionOrderId());
            // 查询物料信息
            com.bmos.lims2.server.material.entity.Material material = null;
            if (order != null && order.getMaterialId() != null) {
                material = materialMapper.selectById(order.getMaterialId());
            }

            // 创建销毁台账记录
            com.bmos.lims2.server.inspect.retention.entity.RetentionDestructionLedger ledger =
                new com.bmos.lims2.server.inspect.retention.entity.RetentionDestructionLedger();
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
            ledger.setQuantity(sample.getCurrentQuantity());  // 销毁时的当前数量
            ledger.setUnitId(sample.getUnitId());
            ledger.setDestructionReason(destructionDTO.getDestructionReason());
            ledger.setDestructionMethod(destructionDTO.getDestructionMethod());
            ledger.setDestructionLocation(destructionDTO.getDestructionLocation());
            ledger.setDestructionTime(destructionDTO.getDestructionTime());
            ledger.setRemark(destructionDTO.getRemark());
            ledger.setDestructorId(destructionDTO.getDestructorId());
            ledger.setDestructorName(destructionDTO.getDestructorName());
            ledger.setSupervisorId(destructionDTO.getSupervisorId());
            ledger.setSupervisorName(destructionDTO.getSupervisorName());

            retentionDestructionLedgerMapper.insert(ledger);
        } catch (Exception e) {
            log.error("记录样品{}销毁台账失败：{}", sample.getSampleNo(), e.getMessage(), e);
            // 不影响主流程，记录日志后继续
        }

        log.info("留样样品销毁成功，样品ID：{}，样品编号：{}，销毁人：{}",
            sampleId, sample.getSampleNo(), destructionDTO.getDestructorName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDestroySamples(java.util.List<Long> sampleIds, com.bmos.lims2.server.inspect.retention.dto.SampleDestructionDTO destructionDTO) {
        if (sampleIds == null || sampleIds.isEmpty()) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLE_IDS_REQUIRED);
        }

        log.info("开始批量销毁样品，样品数量：{}，销毁人：{}", sampleIds.size(), destructionDTO.getDestructorName());

        int successCount = 0;
        int failureCount = 0;
        StringBuilder errorMessages = new StringBuilder();

        for (Long sampleId : sampleIds) {
            try {
                // 为每个样品创建独立的销毁DTO
                com.bmos.lims2.server.inspect.retention.dto.SampleDestructionDTO sampleDestructionDTO =
                    new com.bmos.lims2.server.inspect.retention.dto.SampleDestructionDTO();
                sampleDestructionDTO.setSampleId(sampleId);
                sampleDestructionDTO.setDestructionReason(destructionDTO.getDestructionReason());
                sampleDestructionDTO.setDestructionMethod(destructionDTO.getDestructionMethod());
                sampleDestructionDTO.setDestructionTime(destructionDTO.getDestructionTime());
                sampleDestructionDTO.setDestructionLocation(destructionDTO.getDestructionLocation());
                sampleDestructionDTO.setRemark(destructionDTO.getRemark());
                sampleDestructionDTO.setDestructorId(destructionDTO.getDestructorId());
                sampleDestructionDTO.setDestructorName(destructionDTO.getDestructorName());
                sampleDestructionDTO.setSupervisorId(destructionDTO.getSupervisorId());
                sampleDestructionDTO.setSupervisorName(destructionDTO.getSupervisorName());

                // 调用单个销毁方法
                destroySample(sampleDestructionDTO);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                errorMessages.append("样品ID ").append(sampleId).append(" 销毁失败: ")
                    .append(e.getMessage()).append("; ");
                log.error("批量销毁样品失败，样品ID：{}，错误信息：{}", sampleId, e.getMessage(), e);
            }
        }

        log.info("批量销毁样品完成，成功：{}，失败：{}", successCount, failureCount);

        if (failureCount > 0) {
            throw new BmosException(LimsResponseCode.BUSINESS_ERROR,
                String.format("批量销毁完成，成功%d个，失败%d个。失败详情：%s",
                    successCount, failureCount, errorMessages.toString()));
        }
    }

    @Override
    public com.bmos.lims2.server.inspect.retention.dto.RetentionSamplePrintTagResultDTO getRetentionSamplePrintTag(String sampleNo) {
        if (!org.springframework.util.StringUtils.hasText(sampleNo)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "样品编号不能为空");
        }

        log.info("查询留样样品打印标签信息，样品编号：{}", sampleNo);

        // 查询留样样品打印标签信息
        com.bmos.lims2.server.inspect.retention.dto.RetentionSamplePrintTagResultDTO resultDTO = 
            sampleMapper.selectRetentionSamplePrintTagResult(sampleNo);
        
        if (resultDTO == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "留样样品不存在，样品编号：" + sampleNo);
        }

        // 填充单位名称
        if (resultDTO.getUnitId() != null) {
            resultDTO.setUnitName(unitCache.getGlobalUnitName(resultDTO.getUnitId()));
        }

        // 拼装完整的物料信息（编码-名称）
        if (resultDTO.getMaterialCode() != null && resultDTO.getMaterialName() != null) {
            resultDTO.setFullMaterialName(resultDTO.getMaterialCode() + "-" + resultDTO.getMaterialName());
        }

        // 拼装数量带单位
        if (resultDTO.getQuantity() != null && resultDTO.getUnitName() != null) {
            resultDTO.setQuantityWithUnit(resultDTO.getQuantity() + resultDTO.getUnitName());
        }

        log.info("查询留样样品打印标签信息完成，样品编号：{}，物料：{}", sampleNo, resultDTO.getFullMaterialName());
        
        return resultDTO;
    }
}
