package com.bmos.lims2.server.inspect.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aspose.words.Document;
import com.aspose.words.HtmlLoadOptions;
import com.aspose.words.SaveFormat;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.id.IdUtils;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.division.dto.SampleDivisionPageQueryDTO;
import com.bmos.lims2.server.inspect.document.dto.DocumentConfigFieldDTO;
import com.bmos.lims2.server.inspect.document.dto.DocumentConfigFieldSaveDTO;
import com.bmos.lims2.server.inspect.document.dto.DocumentConfigWithFieldDTO;
import com.bmos.lims2.server.inspect.document.service.DocumentConfigService;
import com.bmos.lims2.server.inspect.order.converter.InspectionOrderConverter;
import com.bmos.lims2.server.inspect.order.converter.InspectionSamplingConverter;
import com.bmos.lims2.server.inspect.order.dto.*;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrderCustomField;
import com.bmos.lims2.server.inspect.order.entity.InspectionSampling;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderCustomFieldMapper;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.order.mapper.InspectionSamplingMapper;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;
// import com.bmos.lims2.server.inspect.order.utils.AsposeInspectionOrderPdfBuilder;
import com.bmos.lims2.server.inspect.sample.dto.SampleDTO;
import com.bmos.lims2.server.inspect.sample.service.SampleService;
// import com.bmos.lims2.server.inspect.order.utils.PdfGeneratorUtil;
import com.bmos.lims2.server.inspect.order.dto.OrderPageDTO;
import com.bmos.lims2.server.inspect.order.dto.SampleReceivePageQueryDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeVersionMapper;
import com.bmos.lims2.server.material.entity.Material;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import com.bmos.lims2.server.material.mapper.MaterialCategoryMapper;
import com.bmos.lims2.server.platform.system.code.PlatformCodeFeignClient;
import com.bmos.lims2.server.platform.system.code.dto.NextCodeVO;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.lims2.server.util.PageUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.lims2.server.task.mapper.TaskStatusHistoryMapper;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.unit.service.UnitCache;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 检验单服务实现类
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Service
@Slf4j
public class InspectionOrderServiceImpl implements InspectionOrderService {

    /**
     * 检验单编号规则代码
     */
    private static final String INSPECTION_ORDER_CODE_RULE = "INSPECTION_ORDER";

    /**
     * 留样检验项目代码
     */
    private static final String SAMPLE_RETENTION_CODE = "SAMPLE_RETENTION";

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private InspectionSamplingMapper inspectionSamplingMapper;

    @Autowired
    private InspectionOrderCustomFieldMapper inspectionOrderCustomFieldMapper;

    @Autowired
    private DocumentConfigService documentConfigService;

    @Autowired
    private PlatformCodeFeignClient platformCodeFeignClient;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialCategoryMapper materialCategoryMapper;

    @Autowired
    private InspectionSchemeVersionMapper inspectionSchemeVersionMapper;

    @Autowired
    private InspectionSchemeMapper inspectionSchemeMapper;

    @Autowired
    private UnitCache unitCache;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskStatusHistoryMapper taskStatusHistoryMapper;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private com.bmos.lims2.server.inspect.item.mapper.InspectItemMapper inspectItemMapper;

    @Override
    public CommonPage<InspectionOrderDTO> getReportPageOrders(InspectionOrderPageQueryDTO queryDTO) {
        queryDTO.setOrderStatus(java.util.Arrays.asList(
                com.bmos.lims2.common.enums.InspectionOrderStatusEnum.COMPLETED,
                com.bmos.lims2.common.enums.InspectionOrderStatusEnum.TERMINATED));
        queryDTO.setSchemeSource(com.bmos.lims2.common.enums.InspectionOrderSourceEnum.REGULAR);
        return getInspectionOrderPage(queryDTO);
    }

    @Override
    public CommonPage<InspectionOrderDTO> getInspectionOrderPage(InspectionOrderPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        if (StringUtils.isNotEmpty(queryDTO.getRequesterName())) {
            ResponseInfo<List<FeignUserVO>> listResponseInfo = FeignUtils.handleRequest(userFeign::getUserByName, queryDTO.getRequesterName());
            if (listResponseInfo.isSuccess()) {
                if (CollUtil.isEmpty(listResponseInfo.getData())) {
                    CommonPage<InspectionOrderDTO> empty = new CommonPage<>();
                    empty.setPageNum(queryDTO.getPageNum());
                    empty.setPageSize(queryDTO.getPageSize());
                    empty.setTotal(0);
                    empty.setList(java.util.Collections.emptyList());
                    return empty;
                }
                queryDTO.setRequesterIds(listResponseInfo.getData().stream().map(FeignUserVO::getUserId).collect(Collectors.toList()));
            } else {
                log.error("获取用户信息失败，平台返回错误：{}", listResponseInfo.getMessage());
                CommonPage<InspectionOrderDTO> empty = new CommonPage<>();
                empty.setPageNum(queryDTO.getPageNum());
                empty.setPageSize(queryDTO.getPageSize());
                empty.setTotal(0);
                empty.setList(java.util.Collections.emptyList());
                return empty;
            }
        }
        List<InspectionOrderDTO> list = inspectionOrderMapper.selectPageWithRelation(queryDTO);
        if (!CollUtil.isEmpty(list)) {
            list.forEach(orderDTO -> {
                orderDTO.setUnitName(unitCache.getGlobalUnitName(orderDTO.getUnitId()));
            });
        }
        return CommonPage.convertPage(list);
    }

    @Override
    public InspectionOrderDTO getInspectionOrderById(Long id) {
        if (id == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "检验单ID不能为空");
        }

        InspectionOrderDTO orderDTO = inspectionOrderMapper.selectByIdWithRelation(id);
        if (orderDTO == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "检验单不存在");
        }

        // 设置单位名称
        if (orderDTO.getUnitId() != null) {
            orderDTO.setUnitName(unitCache.getGlobalUnitName(orderDTO.getUnitId()));
        }
        // 处理自定义字段值
        List<InspectionOrderCustomField> customFieldEntities = inspectionOrderCustomFieldMapper.selectByInspectionOrderId(id);
        if (CollUtil.isNotEmpty(customFieldEntities)) {
            List<CustomFieldValueDTO> customFields = new ArrayList<>();
            for (InspectionOrderCustomField entity : customFieldEntities) {
                CustomFieldValueDTO dto = new CustomFieldValueDTO();
                dto.setFieldCode(entity.getFieldCode());
                dto.setFieldName(entity.getFieldName());
                dto.setFieldValue(entity.getFieldValue());
                dto.setRequired(entity.getRequired());
                dto.setDictCode(entity.getDictCode());
                customFields.add(dto);
            }
            orderDTO.setCustomFields(customFields);
        }

        // 查询并设置取样计划信息
        List<InspectionSamplingDTO> samplingList = inspectionSamplingMapper.selectByInspectionOrderIdWithRelation(id);
        if (CollUtil.isNotEmpty(samplingList)) {
            // 设置单位名称
            for (InspectionSamplingDTO sampling : samplingList) {
                sampling.setUnitName(unitCache.getGlobalUnitName(sampling.getUnitId()));
            }
        }
        orderDTO.setSamplingList(samplingList);

        return orderDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveInspectionOrder(InspectionOrderSaveDTO saveDTO) {
        if (saveDTO == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "保存数据不能为空");
        }

        // 留样验证
        validateRetention(saveDTO);

        InspectionOrder inspectionOrder;

        if (saveDTO.getId() != null) {
            // 更新
            inspectionOrder = inspectionOrderMapper.selectById(saveDTO.getId());
            if (inspectionOrder == null) {
                throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "检验单不存在");
            }

            // 只允许编辑备注、模板和自定义字段
            LambdaUpdateWrapper<InspectionOrder> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(InspectionOrder::getId, inspectionOrder.getId());
            if (saveDTO.getBatchNo() != null) {
                updateWrapper.set(InspectionOrder::getBatchNo, saveDTO.getBatchNo());
            }
            if (saveDTO.getProductionDate() != null) {
                updateWrapper.set(InspectionOrder::getProductionDate, saveDTO.getProductionDate());
            }
            // 模板ID允许为空：为空时需要清空原有模板绑定
            updateWrapper.set(InspectionOrder::getTemplateId, saveDTO.getTemplateId());
            if (saveDTO.getRemark() != null) {
                updateWrapper.set(InspectionOrder::getRemark, saveDTO.getRemark());
            }
            // 更新留样相关字段
            if (saveDTO.getRetentionRequired() != null) {
                updateWrapper.set(InspectionOrder::getRetentionRequired, saveDTO.getRetentionRequired());
            }
            if (saveDTO.getRetentionExpiryDate() != null) {
                updateWrapper.set(InspectionOrder::getRetentionExpiryDate, saveDTO.getRetentionExpiryDate());
            }
            inspectionOrderMapper.update(null, updateWrapper);

            // 更新自定义字段
            updateCustomFields(inspectionOrder.getId(), saveDTO.getCustomFields());

            // 更新取样信息
            updateSamplingInfo(inspectionOrder.getId(), saveDTO.getSamplingList());
        } else {
            // 新增
            inspectionOrder = InspectionOrderConverter.INSTANCE.toEntity(saveDTO);

            // 获取检验单编号（未确认）
            NextCodeVO nextCodeVO = generateOrderNo();
            inspectionOrder.setOrderNo(nextCodeVO.getNo());

            // 设置模板
            inspectionOrder.setTemplateId(saveDTO.getTemplateId());

            // 设置初始状态（请验阶段只设置单据状态）
            inspectionOrder.setOrderStatus(InspectionOrderStatusEnum.PENDING_CONFIRM);

            // 设置来源为常规请验
            inspectionOrder.setSchemeSource(com.bmos.lims2.common.enums.InspectionOrderSourceEnum.REGULAR);

            try {
                // 保存检验单
                inspectionOrderMapper.insert(inspectionOrder);

                // 保存自定义字段
                saveCustomFields(inspectionOrder.getId(), saveDTO.getCustomFields());

                // 保存取样信息
                saveSamplingInfo(inspectionOrder.getId(), saveDTO.getSamplingList());

                // 保存检验项目清单（根据方案版本ID自动生成）
//                saveInspectionItemsList(inspectionOrder.getId(), saveDTO.getSchemeVersionId());

                // 确认编号已使用
                confirmOrderNo(nextCodeVO);

                log.info("检验单创建成功，ID：{}，编号：{}", inspectionOrder.getId(), inspectionOrder.getOrderNo());

            } catch (Exception e) {
                log.error("保存检验单失败，编号：{}，错误：{}", nextCodeVO.getNo(), e.getMessage(), e);
                // 保存失败时不确认编号，让编号可以被重复使用
                throw e;
            }
        }

        return inspectionOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmInspectionOrder(Long id) {
        if (id == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "检验单ID不能为空");
        }

        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(id);
        if (inspectionOrder == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "检验单不存在");
        }

        if (!InspectionOrderStatusEnum.PENDING_CONFIRM.equals(inspectionOrder.getOrderStatus())) {
            throw new BmosException(LimsResponseCode.CONFIRM_ORDER_STATUS_ERROR);
        }

        // 状态流转校验：PENDING_CONFIRM -> CONFIRMED
        com.bmos.lims2.server.inspect.order.util.OrderTransitionGuard.checkOrThrow(
                inspectionOrder.getOrderStatus(), InspectionOrderStatusEnum.CONFIRMED);

        inspectionOrder.setOrderStatus(InspectionOrderStatusEnum.CONFIRMED);
        inspectionOrderMapper.updateById(inspectionOrder);


        // 查询取样计划
        Long inspectionOrderId = inspectionOrder.getId();
        List<InspectionSampling> samplingIds = inspectionSamplingMapper.selectByInspectionOrderId(inspectionOrderId);
        if (CollUtil.isEmpty(samplingIds)) {
            return;
        }
        // 根据取样计划生成样品
        try {
            log.info("检验单ID：{}，开始根据取样计划生成样品", inspectionOrderId);
            List<SampleDTO> generatedSamples = sampleService.generateSamplesFromSampling(inspectionOrderId, samplingIds.stream().map(InspectionSampling::getId).collect(Collectors.toList()));
            log.info("根据取样计划生成样品完成，检验单ID：{}，生成样品数量：{}", inspectionOrderId, generatedSamples.size());
        } catch (Exception e) {
            log.error("根据取样计划生成样品失败，检验单ID：{}，错误：{}", inspectionOrderId, e.getMessage(), e);
            // 样品生成失败不影响检验单保存，但需要记录错误日志
            // 后续可以通过手动补充样品的方式处理
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchConfirmInspectionOrder(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "检验单ID列表不能为空");
        }

        for (Long id : ids) {
            confirmInspectionOrder(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void terminateInspectionOrder(Long id, String terminateReason) {
        if (id == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "检验单ID不能为空");
        }

        if (StrUtil.isBlank(terminateReason)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "终止原因不能为空");
        }

        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(id);
        if (inspectionOrder == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "检验单不存在");
        }

        // 检查是否允许终止（请验阶段的简化规则）
        if (InspectionOrderStatusEnum.TERMINATED.equals(inspectionOrder.getOrderStatus())) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }

        // 状态流转校验：任意 -> TERMINATED
        com.bmos.lims2.server.inspect.order.util.OrderTransitionGuard.checkOrThrow(
                inspectionOrder.getOrderStatus(), InspectionOrderStatusEnum.TERMINATED);

        inspectionOrder.setOrderStatus(InspectionOrderStatusEnum.TERMINATED);
        inspectionOrder.setTerminateReason(terminateReason);
        LocalDateTime now = LocalDateTime.now();
        inspectionOrder.setTerminateTime(now);
        // 终止即视为结束
        inspectionOrder.setFinished(Boolean.TRUE);
        inspectionOrder.setFinishedTime(now);

        inspectionOrderMapper.updateById(inspectionOrder);

        // 同步更新该检验单下所有任务的状态为已终止，并记录任务历史
        java.util.List<Task> tasks = taskMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Task>()
                        .eq(Task::getInspectionOrderId, id)
        );
        LambdaUpdateWrapper<Task> uw = new LambdaUpdateWrapper<>();
        uw.eq(Task::getInspectionOrderId, id)
                .set(Task::getStatus, TaskStatusEnum.TERMINATED);
        taskMapper.update(null, uw);

        // 写入历史
        java.time.LocalDateTime now2 = java.time.LocalDateTime.now();
        for (Task t : tasks) {
            com.bmos.lims2.server.task.entity.TaskStatusHistory h = new com.bmos.lims2.server.task.entity.TaskStatusHistory();
            h.setTaskId(t.getId());
            h.setOperationType(com.bmos.lims2.common.enums.TaskOperationTypeEnum.TERMINATED);
            h.setFromStatus(t.getStatus() == null ? null : t.getStatus().getValue());
            h.setToStatus(TaskStatusEnum.TERMINATED.getValue());
            h.setOperatorId(com.bmos.common.holder.SysUserHolder.getUser().getUserId());
            h.setOperateTime(now2);
            try {
                taskStatusHistoryMapper.insert(h);
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionOrder(Long id) {
        if (id == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "检验单ID不能为空");
        }

        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(id);
        if (inspectionOrder == null) {
            throw new BmosException(LimsResponseCode.DATA_NOT_EXISTS, "检验单不存在");
        }

        // 只有待确认状态的检验单才能删除
        if (!InspectionOrderStatusEnum.PENDING_CONFIRM.equals(inspectionOrder.getOrderStatus())) {
            throw new BmosException(LimsResponseCode.FLOW_AUDIT_PARAMETER_ERROR);
        }

        // 删除取样信息
        inspectionSamplingMapper.deleteByInspectionOrderId(id);

        // 删除自定义字段值
        inspectionOrderCustomFieldMapper.deleteByInspectionOrderId(id);

        // 删除检验单
        inspectionOrderMapper.deleteById(id);
    }

    @Override
    public byte[] generateInspectionOrderPdf(Long id) {
        if (id == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "检验单ID不能为空");
        }

        InspectionOrderDTO orderDTO = getInspectionOrderById(id);

        // 先生成HTML，再使用Aspose将HTML转换为PDF
        try {
            String htmlContent = generateHtmlContent(orderDTO);

            String wellFormedHtml = ensureWellFormedHtmlForAspose(htmlContent);

            HtmlLoadOptions loadOptions = new HtmlLoadOptions(StandardCharsets.UTF_8.name());
            Document document = new Document(new ByteArrayInputStream(wellFormedHtml.getBytes(StandardCharsets.UTF_8)), loadOptions);

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                document.save(out, SaveFormat.PDF);
                return out.toByteArray();
            }
        } catch (Exception e) {
            log.error("生成检验单PDF失败", e);
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "生成PDF失败: " + e.getMessage());
        }
    }

    @Override
    public byte[] previewInspectionOrderPdfByConfigId(Long configId) {
        if (configId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "请验单配置ID不能为空");
        }

        // 使用配置构造一个最小化的 InspectionOrderDTO，仅用于预览打印
        DocumentConfigWithFieldDTO config = documentConfigService.queryDetail(configId);
        if (config == null) {
            throw new BmosException(LimsResponseCode.INSPECTION_CONFIG_NOT_EXISTS);
        }

        InspectionOrderDTO dto = new InspectionOrderDTO();
        // 基础信息可为空，预览时展示空白
        dto.setSamplingList(java.util.Collections.emptyList());

        // 自定义字段按配置顺序渲染，值留空或使用默认值
        if (config.getDataList() != null && !config.getDataList().isEmpty()) {
            java.util.List<CustomFieldValueDTO> fields = new java.util.ArrayList<>();
            for (com.bmos.lims2.server.inspect.document.dto.DocumentConfigFieldDTO fieldCfg : config.getDataList()) {
                CustomFieldValueDTO f = new CustomFieldValueDTO();
                f.setFieldCode(fieldCfg.getCode());
                f.setFieldName(fieldCfg.getShowName());
                f.setFieldValue(fieldCfg.getDefaultValue());
                f.setRequired(fieldCfg.getRequired());
                fields.add(f);
            }
            dto.setCustomFields(fields);
        }

        try {
            String htmlContent = generateHtmlContent(dto);
            String wellFormedHtml = ensureWellFormedHtmlForAspose(htmlContent);
            HtmlLoadOptions loadOptions = new HtmlLoadOptions(java.nio.charset.StandardCharsets.UTF_8.name());
            com.aspose.words.Document document = new com.aspose.words.Document(new java.io.ByteArrayInputStream(wellFormedHtml.getBytes(java.nio.charset.StandardCharsets.UTF_8)), loadOptions);
            try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
                document.save(out, com.aspose.words.SaveFormat.PDF);
                return out.toByteArray();
            }
        } catch (Exception e) {
            log.error("预览请验单PDF失败", e);
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "预览PDF失败: " + e.getMessage());
        }
    }

    @Override
    public byte[] previewInspectionOrderPdfByConfigId(List<DocumentConfigFieldSaveDTO> inputFields) {

        // 构造最小DTO并合并字段：输入值 > 默认值 > 空
        InspectionOrderDTO dto = new InspectionOrderDTO();
        dto.setSamplingList(java.util.Collections.emptyList());


        java.util.List<CustomFieldValueDTO> fields = new java.util.ArrayList<>();
        for (DocumentConfigFieldSaveDTO fieldCfg : inputFields) {
            CustomFieldValueDTO f = new CustomFieldValueDTO();
            f.setFieldCode(fieldCfg.getCode());
            f.setFieldName(fieldCfg.getShowName());
            f.setFieldValue(fieldCfg.getDefaultValue());
            f.setRequired(fieldCfg.getRequired());
            fields.add(f);
        }
        dto.setCustomFields(fields);

        try {
            String htmlContent = generateHtmlContent(dto);
            String wellFormedHtml = ensureWellFormedHtmlForAspose(htmlContent);
            HtmlLoadOptions loadOptions = new HtmlLoadOptions(java.nio.charset.StandardCharsets.UTF_8.name());
            com.aspose.words.Document document = new com.aspose.words.Document(new java.io.ByteArrayInputStream(wellFormedHtml.getBytes(java.nio.charset.StandardCharsets.UTF_8)), loadOptions);
            try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
                document.save(out, com.aspose.words.SaveFormat.PDF);
                return out.toByteArray();
            }
        } catch (Exception e) {
            log.error("预览请验单PDF失败", e);
            throw new BmosException(LimsResponseCode.SYSTEM_ERROR, "预览PDF失败: " + e.getMessage());
        }
    }

    @Override
    public InspectionOrderDTO getInspectionOrderByOrderNo(String orderNo) {
        if (StrUtil.isBlank(orderNo)) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "检验单号不能为空");
        }

        InspectionOrder inspectionOrder = inspectionOrderMapper.selectByOrderNo(orderNo);
        if (inspectionOrder == null) {
            return null;
        }

        return getInspectionOrderById(inspectionOrder.getId());
    }

    @Override
    public boolean existsInspectionOrder(Long id) {
        if (id == null) {
            return false;
        }
        return inspectionOrderMapper.existsById(id);
    }

    /**
     * 保存自定义字段值
     *
     * @param inspectionOrderId 检验单ID
     * @param customFields      自定义字段值列表
     */
    private void saveCustomFields(Long inspectionOrderId, List<CustomFieldValueDTO> customFields) {
        if (CollUtil.isEmpty(customFields)) {
            return;
        }

        List<InspectionOrderCustomField> entities = new ArrayList<>();
        for (int i = 0; i < customFields.size(); i++) {
            CustomFieldValueDTO dto = customFields.get(i);
            InspectionOrderCustomField entity = new InspectionOrderCustomField();
            entity.setInspectionOrderId(inspectionOrderId);
            entity.setFieldCode(dto.getFieldCode());
            entity.setFieldName(dto.getFieldName());
            entity.setFieldValue(dto.getFieldValue());
            entity.setRequired(dto.getRequired());
            entity.setDictCode(dto.getDictCode());
            entity.setSort(i + 1); // 设置排序
            entities.add(entity);
        }

        inspectionOrderCustomFieldMapper.batchInsert(entities);
    }

    /**
     * 更新自定义字段值
     *
     * @param inspectionOrderId 检验单ID
     * @param customFields      自定义字段值列表
     */
    private void updateCustomFields(Long inspectionOrderId, List<CustomFieldValueDTO> customFields) {
        // 先删除原有的自定义字段值
        inspectionOrderCustomFieldMapper.deleteByInspectionOrderId(inspectionOrderId);

        // 再保存新的自定义字段值
        saveCustomFields(inspectionOrderId, customFields);
    }

    /**
     * 生成检验单号（未确认使用）
     *
     * @return NextCodeVO 包含编号信息
     */
    private NextCodeVO generateOrderNo() {
        log.info("调用平台接口获取检验单编号，编号规则代码：{}", INSPECTION_ORDER_CODE_RULE);
        return platformCodeFeignClient.getInspectOrderNextUseNo(INSPECTION_ORDER_CODE_RULE);
    }

    /**
     * 确认检验单编号已使用
     *
     * @param nextCodeVO 编号信息
     */
    private void confirmOrderNo(NextCodeVO nextCodeVO) {
        log.info("确认检验单编号已使用，编号：{}", nextCodeVO.getNo());
        platformCodeFeignClient.confirmInspectOrderNo(nextCodeVO.getCode(), nextCodeVO.getNo());
    }

//    /**
//     * 保存检验项目清单（根据检验方案版本ID自动生成）
//     * 按照正确的业务层级：检验方案版本 → 实验包配置 → 检验项目 → 分析项 → 数据点
//     *
//     * @param inspectionOrderId 检验单ID
//     * @param schemeVersionId   检验方案版本ID
//     */
//    private void saveInspectionItemsList(Long inspectionOrderId, Long schemeVersionId) {
//        if (schemeVersionId == null) {
//            log.warn("检验方案版本ID为空，跳过保存检验项目清单，检验单ID：{}", inspectionOrderId);
//            return;
//        }
//
//        try {
//            log.info("开始根据检验方案版本生成执行清单，检验单ID：{}，方案版本ID：{}", inspectionOrderId, schemeVersionId);
//
//            // 1. 查询检验方案详情（实验包配置）
//            InspectionSchemeVersionFullConfigDTO inspectionSchemeVersionFullConfig = inspectionSchemeVersionService.getInspectionSchemeVersionFullConfig(schemeVersionId);
//
//            if (inspectionSchemeVersionFullConfig == null) {
//                log.warn("检验方案版本无配置详情，跳过保存，检验单ID：{}，方案版本ID：{}", inspectionOrderId, schemeVersionId);
//                return;
//            }
//
//            int globalSort = 0;
//
//            // 2. 遍历检验方案明细
//            log.info("处理检验方案明细，检品ID：{}，实验包ID：{}", inspectionSchemeVersionFullConfig.getMaterial().getMaterialId(), inspectionSchemeVersionFullConfig.getPackageInfo().getPackageId());
//
//            // 3. 直接使用检验方案中已配置的检验项目
//            List<InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO> inspectionItems = inspectionSchemeVersionFullConfig.getInspectionItems();
//
//            if (CollUtil.isEmpty(inspectionItems)) {
//                log.warn("检验方案明细无检验项目配置，跳过，明细ID：{}", inspectionSchemeVersionFullConfig.getId());
//                return;
//            }
//
//            // 4. 遍历检验项目配置
//            for (InspectionSchemeVersionFullConfigDTO.InspectionItemConfigDTO inspectionItem : inspectionItems) {
//                Long inspectItemId = inspectionItem.getInspectItemId();
//                String inspectItemName = inspectionItem.getInspectItemName();
//                String inspectItemCode = inspectionItem.getInspectItemCode();
//
//                // 4.1. 保存检验项目清单
//                InspectionOrderItem orderItem = new InspectionOrderItem();
//                orderItem.setInspectionOrderId(inspectionOrderId);
//                orderItem.setInspectItemId(inspectItemId);
//                orderItem.setInspectItemName(inspectItemName);
//                orderItem.setInspectItemCode(inspectItemCode);
//                orderItem.setSort(globalSort++);
//                inspectionOrderItemMapper.insert(orderItem);
//
//                log.info("保存检验项目清单，项目：{}[{}]", inspectItemName, inspectItemCode);
//
//                // 5. 直接使用检验项目中已配置的分析项列表
//                List<InspectionSchemeVersionFullConfigDTO.AnalyzeItemConfigDTO> parameters = inspectionItem.getAnalyzeItems();
//
//                if (CollUtil.isEmpty(parameters)) {
//                    log.warn("检验项目配置无分析项，跳过，检验项目ID：{}", inspectItemId);
//                    continue;
//                }
//
//                // 6. 遍历分析项配置
//                int parameterSort = 0;
//                for (InspectionSchemeVersionFullConfigDTO.AnalyzeItemConfigDTO parameter : parameters) {
//                    Long parameterId = parameter.getParameterId();
//                    String parameterName = parameter.getParameterName();
//                    String parameterCode = parameter.getParameterCode();
//
//                    // 6.1. 保存分析项清单
//                    InspectionOrderParameter orderParameter = new InspectionOrderParameter();
//                    orderParameter.setInspectionOrderId(inspectionOrderId);
//                    orderParameter.setOrderItemId(orderItem.getId());
//                    orderParameter.setParameterId(parameterId);
//                    orderParameter.setParameterName(parameterName);
//                    orderParameter.setParameterCode(parameterCode);
//                    orderParameter.setSort(parameterSort++);
//                    inspectionOrderParameterMapper.insert(orderParameter);
//
//                    log.info("保存分析项清单，分析项：{}[{}]", parameterName, parameterCode);
//
//                    // 7. 直接使用分析项中已配置的数据点列表
//                    List<InspectionSchemeVersionFullConfigDTO.DataPointConfigDTO> dataPoints = parameter.getDataPoints();
//
//                    if (CollUtil.isEmpty(dataPoints)) {
//                        log.warn("分析项配置无数据点，跳过，分析项ID：{}", parameterId);
//                        continue;
//                    }
//
//                    // 7.1. 保存数据点清单
//                    int dataPointSort = 0;
//                    for (InspectionSchemeVersionFullConfigDTO.DataPointConfigDTO dataPoint : dataPoints) {
//                        InspectionOrderDataPoint orderDataPoint = new InspectionOrderDataPoint();
//                        orderDataPoint.setInspectionOrderId(inspectionOrderId);
//                        orderDataPoint.setOrderParameterId(orderParameter.getId());
//                        orderDataPoint.setDataPointId(dataPoint.getOriginalPointId());
//                        orderDataPoint.setDataPointName(dataPoint.getName());
//                        orderDataPoint.setSort(dataPointSort++);
//                        inspectionOrderDataPointMapper.insert(orderDataPoint);
//
//                        log.info("保存数据点清单，数据点：{}", dataPoint.getName());
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("保存检验项目清单失败，检验单ID：{}，方案版本ID：{}，错误：{}", inspectionOrderId, schemeVersionId, e.getMessage(), e);
//            throw new RuntimeException("保存检验项目清单失败：" + e.getMessage(), e);
//        }
//    }

    /**
     * 保存取样信息
     *
     * @param inspectionOrderId 检验单ID
     * @param samplingList      取样信息列表
     */
    private void saveSamplingInfo(Long inspectionOrderId, List<InspectionSamplingSaveDTO> samplingList) {
        if (CollUtil.isEmpty(samplingList)) {
            return;
        }
        // 保存取样计划
        for (InspectionSamplingSaveDTO samplingDTO : samplingList) {
            InspectionSampling sampling = InspectionSamplingConverter.INSTANCE.toEntity(samplingDTO);
            sampling.setId(IdUtils.getSnowflake());
            sampling.setInspectionOrderId(inspectionOrderId);
            // 取样计划不需要设置状态，状态由后续的样品管理功能维护
            inspectionSamplingMapper.insert(sampling);
            log.info("取样计划保存成功，ID：{}，检验单ID：{}", sampling.getId(), inspectionOrderId);
        }
    }

    /**
     * 更新取样信息
     *
     * @param inspectionOrderId 检验单ID
     * @param samplingList      取样信息列表
     */
    private void updateSamplingInfo(Long inspectionOrderId, List<InspectionSamplingSaveDTO> samplingList) {
        // 删除原有取样信息
        inspectionSamplingMapper.deleteByInspectionOrderId(inspectionOrderId);

        // 重新保存取样信息
        saveSamplingInfo(inspectionOrderId, samplingList);
    }

    /**
     * 生成HTML内容用于PDF生成
     *
     * @param orderDTO 检验单信息
     * @return HTML内容
     */
    private String generateHtmlContent(InspectionOrderDTO orderDTO) {
        StringBuilder html = new StringBuilder();

        // 标题
        html.append("<h1>请验单</h1>");

        // 四列表格（label + value + label + value），分析项/备注合并右侧三列
        html.append("<table class=\"info\">");
        html.append("<colgroup>");
        html.append("<col style=\"width:18%\"/>");
        html.append("<col style=\"width:32%\"/>");
        html.append("<col style=\"width:18%\"/>");
        html.append("<col style=\"width:32%\"/>");
        html.append("</colgroup>");

        // 1 行：检品信息 / 检品规格
        html.append("<tr>")
                .append("<td class=\"label\">检品信息</td>")
                .append("<td>").append(nz(joinWithHyphen(orderDTO.getMaterialCode(), orderDTO.getMaterialName()))).append("</td>")
                .append("<td class=\"label\">检品规格</td>")
                .append("<td>").append(nz(orderDTO.getMaterialSpec())).append("</td>")
                .append("</tr>");

        // 2 行：所属分类 / 单位
        String categoryName = resolveMaterialCategoryName(orderDTO.getMaterialId());
        html.append("<tr>")
                .append("<td class=\"label\">所属分类</td>")
                .append("<td>").append(nz(categoryName)).append("</td>")
                .append("<td class=\"label\">单位</td>")
                .append("<td>").append(nz(orderDTO.getUnitName())).append("</td>")
                .append("</tr>");

        // 3 行：检验方案 / 请验单（模板）- 如果没有模板则不显示请验单项
        String templateName = orderDTO.getTemplateName();
        if (templateName != null && !templateName.trim().isEmpty()) {
            // 有模板：显示完整的4列
            html.append("<tr>")
                    .append("<td class=\"label\">检验方案</td>")
                    .append("<td>").append(nz(joinWithHyphen(orderDTO.getSchemeName(), orderDTO.getSchemeVersion()))).append("</td>")
                    .append("<td class=\"label\">请验单</td>")
                    .append("<td>").append(nz(templateName)).append("</td>")
                    .append("</tr>");
        } else {
            // 无模板：只显示检验方案，值跨3列，不显示请验单项
            html.append("<tr>")
                    .append("<td class=\"label\">检验方案</td>")
                    .append("<td colspan=\"3\">").append(nz(joinWithHyphen(orderDTO.getSchemeName(), orderDTO.getSchemeVersion()))).append("</td>")
                    .append("</tr>");
        }

        // 4 行：批号 / 检验单号
        html.append("<tr>")
                .append("<td class=\"label\">批号</td>")
                .append("<td>").append(nz(orderDTO.getBatchNo())).append("</td>")
                .append("<td class=\"label\">检验单号</td>")
                .append("<td>").append(nz(orderDTO.getOrderNo())).append("</td>")
                .append("</tr>");

        // 5 行：请验人 / 请验时间
        String reqTime = orderDTO.getRequestTime() == null ? "" : DateTimeFormatter.ofPattern("yyyy/MM/dd").format(orderDTO.getRequestTime());
        html.append("<tr>")
                .append("<td class=\"label\">请验人</td>")
                .append("<td>").append(nz(orderDTO.getRequestUserName())).append("</td>")
                .append("<td class=\"label\">请验时间</td>")
                .append("<td>").append(reqTime).append("</td>")
                .append("</tr>");

        // 6 行：分析项（右侧合并三列）
        html.append("<tr>")
                .append("<td class=\"label\">分析项</td>")
                .append("<td class=\"value\" colspan=\"3\">")
                .append(joinAnalyzeItems(orderDTO.getSamplingList()))
                .append("</td>")
                .append("</tr>");

        // 自定义字段：按 2 对 label-value 每行渲染
        if (CollUtil.isNotEmpty(orderDTO.getCustomFields())) {
            java.util.List<CustomFieldValueDTO> fields = orderDTO.getCustomFields();
            for (int i = 0; i < fields.size(); i += 2) {
                CustomFieldValueDTO f1 = fields.get(i);
                CustomFieldValueDTO f2 = (i + 1 < fields.size()) ? fields.get(i + 1) : null;

                String name1 = f1.getFieldName() != null ? f1.getFieldName() : f1.getFieldCode();
                String val1 = nz(f1.getFieldValue());
                String name2 = f2 == null ? "" : (f2.getFieldName() != null ? f2.getFieldName() : f2.getFieldCode());
                String val2 = f2 == null ? "" : nz(f2.getFieldValue());

                html.append("<tr>")
                        .append("<td class=\"label\">").append(nz(name1)).append("</td>")
                        .append("<td>").append(val1).append("</td>")
                        .append("<td class=\"label\">").append(nz(name2)).append("</td>")
                        .append("<td>").append(val2).append("</td>")
                        .append("</tr>");
            }
        }

        // 6 行：备注（右侧合并三列）
        html.append("<tr>")
                .append("<td class=\"label\">备注</td>")
                .append("<td class=\"value\" colspan=\"3\">")
                .append(nz(orderDTO.getRemark()))
                .append("</td>")
                .append("</tr>");

        html.append("</table>");

        return html.toString();
    }

    /**
     * 确保HTML对Aspose友好：补充DOCTYPE、html/head/body以及基础样式和UTF-8声明
     */
    private String ensureWellFormedHtmlForAspose(String htmlContent) {
        String trimmed = htmlContent == null ? "" : htmlContent.trim();
        String lower = trimmed.toLowerCase();
        if (lower.startsWith("<!doctype") || lower.startsWith("<html")) {
            return trimmed;
        }
        StringBuilder wellFormed = new StringBuilder();
        wellFormed.append("<!DOCTYPE html>\n");
        wellFormed.append("<html>\n");
        wellFormed.append("<head>\n");
        wellFormed.append("<meta charset=\"UTF-8\"/>\n");
        wellFormed.append("<title>请验单</title>\n");
        wellFormed.append("<style>body{font-family:SimSun,serif;font-size:12px;}table{border-collapse:collapse;width:100%;}table,th,td{border:1px solid #000;}th,td{padding:5px;text-align:left;}h1{text-align:center;}.label{font-weight:bold;}</style>\n");
        wellFormed.append("</head>\n");
        wellFormed.append("<body>\n");
        wellFormed.append(trimmed);
        wellFormed.append("\n</body>\n");
        wellFormed.append("</html>");
        return wellFormed.toString();
    }

    /**
     * 解析物料分类名称
     */
    private String resolveMaterialCategoryName(Long materialId) {
        try {
            if (materialId == null) {
                return "";
            }
            Material material = materialMapper.selectById(materialId);
            if (material == null || material.getCategoryId() == null) {
                return "";
            }
            com.bmos.lims2.server.material.entity.MaterialCategory mc = materialCategoryMapper.selectById(material.getCategoryId());
            return mc == null ? "" : nz(mc.getName());
        } catch (Exception ignore) {
            return "";
        }
    }

    /**
     * 将取样计划中的检验项目名称去重并按原顺序拼接
     */
    private String joinAnalyzeItems(java.util.List<InspectionSamplingDTO> samplingList) {
        if (samplingList == null || samplingList.isEmpty()) {
            return "";
        }
        java.util.Set<String> items = new java.util.LinkedHashSet<>();
        for (InspectionSamplingDTO s : samplingList) {
            if (s != null) {
                String name = s.getInspectItemName();
                if (name != null && !name.trim().isEmpty()) {
                    items.add(name);
                }
            }
        }
        return items.stream().collect(java.util.stream.Collectors.joining("、"));
    }

    /**
     * null 安全的字符串
     */
    private String nz(String s) {
        return s == null ? "" : s;
    }

    /**
     * 将两个字段用连字符连接，空值会被忽略；都为空返回空串
     */
    private String joinWithHyphen(String left, String right) {
        String a = left == null ? "" : left.trim();
        String b = right == null ? "" : right.trim();
        boolean hasA = !a.isEmpty();
        boolean hasB = !b.isEmpty();
        if (hasA && hasB) {
            return a + "-" + b;
        }
        if (hasA) {
            return a;
        }
        if (hasB) {
            return b;
        }
        return "";
    }

    @Override
    public List<InspectionOrderTemplateDTO> getAvailableTemplatesByMaterialId(Long materialId) {
        if (materialId == null) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "检品ID不能为空");
        }

        // 调用现有的DocumentConfigService获取绑定的模板列表
        List<DocumentConfigWithFieldDTO> configList = documentConfigService.getInspectionConfigListByProductId(materialId);

        if (CollUtil.isEmpty(configList)) {
            return new ArrayList<>();
        }

        // 转换为请验单模板DTO
        List<InspectionOrderTemplateDTO> templateList = new ArrayList<>();
        for (DocumentConfigWithFieldDTO config : configList) {
            InspectionOrderTemplateDTO template = new InspectionOrderTemplateDTO();
            template.setId(config.getId());
            template.setName(config.getName());
            template.setRemark(config.getRemark());
            template.setStatus(config.getStatus());

            // 转换字段列表
            if (CollUtil.isNotEmpty(config.getDataList())) {
                List<InspectionOrderTemplateFieldDTO> fieldList = new ArrayList<>();
                for (DocumentConfigFieldDTO fieldDTO : config.getDataList()) {
                    InspectionOrderTemplateFieldDTO field = new InspectionOrderTemplateFieldDTO();
                    field.setId(fieldDTO.getId());
                    field.setCode(fieldDTO.getCode());
                    field.setShowName(fieldDTO.getShowName());
                    field.setDataName(fieldDTO.getDataName());
                    field.setRequired(fieldDTO.getRequired());
                    field.setDefaultValue(fieldDTO.getDefaultValue());
                    field.setSort(fieldDTO.getSort());
                    fieldList.add(field);
                }
                template.setFields(fieldList);
            }

            templateList.add(template);
        }

        return templateList;
    }

    @Override
    public CommonPage<InspectionOrderDTO> getConfirmedOrders(InspectionOrderPageQueryDTO queryDTO) {
        // 分页查询
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<InspectionOrderDTO> result = inspectionOrderMapper.selectConfirmedOrders(queryDTO);

        for (InspectionOrderDTO order : result) {
            // 填充方案信息
            if (order.getSchemeVersionId() != null) {
                InspectionSchemeVersion version = inspectionSchemeVersionMapper.selectById(order.getSchemeVersionId());
                if (version != null) {
                    order.setSchemeVersion(version.getVersionNo());

                    // 获取方案名称
                    InspectionScheme scheme = inspectionSchemeMapper.selectById(version.getSchemeId());
                    if (scheme != null) {
                        order.setSchemeName(scheme.getName());
                    }
                }
            }
        }

        return CommonPage.convertPage(result);
    }


    @Override
    public CommonPage<OrderPageDTO> getPendingReceiveOrders(SampleReceivePageQueryDTO queryDTO) {
        log.info("分页查询待接收的检验单列表，查询条件：{}", queryDTO);
        // 查询待接收的检验单（存在已取样但未接收的样品）
        // 分页查询
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<OrderPageDTO> result = inspectionOrderMapper.selectPendingReceiveOrders(queryDTO);
        return CommonPage.convertPage(result);
    }


    @Override
    public CommonPage<OrderPageDTO> pageSampleDivisionOrders(SampleDivisionPageQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        log.info("分页查询待分样检验单列表，查询条件：{}", queryDTO);
        // 执行查询
        List<OrderPageDTO> pageResult = inspectionOrderMapper.selectPageWithSamplesForDivision(queryDTO);
        return CommonPage.convertPage(pageResult);
    }

    @Override
    public CommonPage<InspectionOrderOptionDTO> listOptions(InspectionOrderOptionQueryDTO queryDTO) {
        // 统一分页风格：PageHelper + CommonPage
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        List<InspectionOrderOptionDTO> list = inspectionOrderMapper.listOptions(queryDTO);
        return CommonPage.convertPage(list);
    }

    @Override
    public java.util.List<InspectionOrderOptionDTO> listOptionsByScheme(InspectionOrderBySchemeQueryDTO queryDTO) {
        return inspectionOrderMapper.listOptionsByScheme(queryDTO);
    }

    @Override
    public long countConfirmedOrders() {
        return inspectionOrderMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<InspectionOrder>()
                        .eq(InspectionOrder::getOrderStatus, InspectionOrderStatusEnum.CONFIRMED));
    }

    @Override
    public long countConfirmedOrdersForSampling() {
        return inspectionOrderMapper.countConfirmedOrdersForSampling();
    }

    /**
     * 验证留样相关参数
     *
     * @param saveDTO 保存DTO
     */
    private void validateRetention(InspectionOrderSaveDTO saveDTO) {
        // 如果不需要留样,则不验证
        if (saveDTO.getRetentionRequired() == null || !saveDTO.getRetentionRequired()) {
            return;
        }

        // 需要留样时,有效期至必填
        if (saveDTO.getRetentionExpiryDate() == null) {
            throw new BmosException(LimsResponseCode.RETENTION_EXPIRY_DATE_REQUIRED);
        }

        // 需要留样时,取样信息中必须包含留样检验项目
        if (CollUtil.isEmpty(saveDTO.getSamplingList())) {
            throw new BmosException(LimsResponseCode.RETENTION_SAMPLING_REQUIRED);
        }

        // 获取留样检验项目ID
        Long retentionItemId = getRetentionInspectItemId();
        if (retentionItemId == null) {
            throw new BmosException(LimsResponseCode.RETENTION_INSPECT_ITEM_NOT_CONFIGURED);
        }

        // 验证取样信息中是否包含留样检验项目
        boolean hasRetentionSampling = saveDTO.getSamplingList().stream()
                .anyMatch(sampling -> retentionItemId.equals(sampling.getInspectItemId()));

        if (!hasRetentionSampling) {
            throw new BmosException(LimsResponseCode.RETENTION_INSPECT_ITEM_REQUIRED);
        }
    }

    /**
     * 获取留样检验项目ID
     *
     * @return 留样检验项目ID,如果不存在则返回null
     */
    private Long getRetentionInspectItemId() {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.bmos.lims2.server.inspect.item.entity.InspectItem> queryWrapper
                = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(com.bmos.lims2.server.inspect.item.entity.InspectItem::getCode, SAMPLE_RETENTION_CODE);
        com.bmos.lims2.server.inspect.item.entity.InspectItem retentionItem = inspectItemMapper.selectOne(queryWrapper);
        return retentionItem != null ? retentionItem.getId() : null;
    }
}