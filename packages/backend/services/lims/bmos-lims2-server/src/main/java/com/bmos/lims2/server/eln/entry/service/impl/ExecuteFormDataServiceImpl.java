package com.bmos.lims2.server.eln.entry.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.bmos.ExpressionCalculator;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.common.enums.*;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.config.minio.MinioFileClient;
import com.bmos.lims2.server.config.minio.constants.MinioBucket;
import com.bmos.lims2.server.eln.entry.constant.RedissionKeyConstant;
import com.bmos.lims2.server.eln.entry.converter.ExecuteAttachmentConvert;
import com.bmos.lims2.server.eln.entry.converter.ExecuteFormDataConverter;
import com.bmos.lims2.server.eln.entry.dto.*;
import com.bmos.lims2.server.eln.entry.entity.ExecuteAttachment;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.entry.enums.ExecuteFormDataType;
import com.bmos.lims2.server.eln.entry.mapper.ExecuteFormDataMapper;
import com.bmos.lims2.server.eln.entry.service.ExecuteAttachmentService;
import com.bmos.lims2.server.eln.entry.service.ExecuteFormDataHandleService;
import com.bmos.lims2.server.eln.entry.service.ExecuteFormDataService;
import com.bmos.lims2.server.eln.entry.vo.AttachmentVO;
import com.bmos.lims2.server.eln.entry.vo.FieldConfigVO;
import com.bmos.lims2.server.eln.entry.vo.FormDataItemVO;
import com.bmos.lims2.server.eln.entry.vo.FormDataVO;
import com.bmos.lims2.server.eln.conclusion.dto.ConclusionComponentSaveDTO;
import com.bmos.lims2.server.inspect.entry.dto.BatchJudgmentDTO;
import com.bmos.lims2.server.eln.record.dto.CalculateParam;
import com.bmos.lims2.server.eln.record.dto.CalculateResult;
import com.bmos.lims2.server.eln.record.dto.FormulaFieldDTO;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.entity.formula.ComponentFormulaConfig;
import com.bmos.lims2.server.eln.record.enums.ComponentFormulaTypeEnum;
import com.bmos.lims2.server.eln.record.service.BatchRecordComponentService;
import com.bmos.lims2.server.eln.record.util.Graph;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeVersionMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeVersionService;
import com.bmos.lims2.server.inspect.entry.dto.BatchEntryDTO;
import com.bmos.lims2.server.inspect.entry.service.InspectionEntryService;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeDataPoint;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeDataPointMapper;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeDataPoint;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeDataPointMapper;
import com.bmos.lims2.server.material.mapper.MaterialMapper;
import com.bmos.lims2.server.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.lims2.server.task.entity.Task;
import com.bmos.lims2.server.task.mapper.TaskMapper;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.unit.service.UnitCache;
import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExecuteFormDataServiceImpl implements ExecuteFormDataService {
    private static final String RECORD_ERROR_DATA_CODE = "mes.record.error-data";
    private static final String RECORD_EMPTY_DATA ="mes.record.empty-data";
    @Autowired
    private PlatformParameterClientImpl platformParameterClientImpl;
    @Autowired
    private ExecuteFormDataMapper executeFormDataMapper;

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private BatchRecordComponentService batchRecordComponentService;
    @Autowired
    private ExpressionCalculator expressionCalculator;
    @Autowired
    private ExecuteAttachmentService executeAttachmentService;

//    @Autowired
//    private Map<String, BusinessComponentStrategy> strategyMap;

    @Resource
    private MinioFileClient minioFileClient;

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private InspectionSchemeParameterMapper inspectionSchemeParameterMapper;

    @Autowired
    private StabilitySchemeParameterMapper stabilitySchemeParameterMapper;

    @Autowired
    private StabilitySchemeVersionMapper stabilitySchemeVersionMapper;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private InspectionSchemeVersionService inspectionSchemeVersionService;

    @Resource
    private ExecuteFormDataHandleService formDataHandleService;



    @Autowired
    private UnitCache unitCache;

    @Autowired
    private InspectionEntryService inspectionEntryService;

    @Autowired
    private InspectionSchemeDataPointMapper inspectionSchemeDataPointMapper;

    @Autowired
    private StabilitySchemeDataPointMapper stabilitySchemeDataPointMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private com.bmos.lims2.server.task.mapper.TaskStatusHistoryMapper taskStatusHistoryMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(FormDataBatchSaveDTO dto) {
        this.syncLimsConclusion(dto);
        RLock lock = redissonClient.getLock(String.format(RedissionKeyConstant.EXECUTE_EXPRESS,
                dto.getInspectionOrderId()));
        boolean lockResult = lock.tryLock();
        if (!lockResult) {
            throw new BmosException(LimsResponseCode.PROCEDURE_EXPRESS_LOCKED);
        }
        try {
            List<ExecuteFormData> dataList = ExecuteFormDataConverter.INSTANCE.convert(dto);
            //判断是否是存在拍照上传组件，拍照上传组件保存图片附件，value保存附件id
            if (CollUtil.isNotEmpty(dataList)) {
                savePicture(dataList, dto.getRecordVersionId());
            }
            executeFormDataMapper.insertBatch(dataList);
            List<ExecuteFormData> resultData = calculateData(
                    ExecuteFormDataConverter.INSTANCE.convert(dto),
                    ExecuteFormDataConverter.INSTANCE.convertQuery(dto));
            if (CollUtil.isNotEmpty(resultData)) {
                Long nextRev = selectMaxRev(dto.getInspectionOrderId(), CollectionUtils.convertSet(resultData, ExecuteFormData::getFieldId));
                resultData.forEach(e -> e.setRev(nextRev));
                executeFormDataMapper.insertBatch(resultData);
            }
            // 联动检验录入：将记录的“最终结果”同步到数据点（与 /api/inspection-entry/batch-save 一致逻辑）
            tryPropagateToInspectionEntry(dto, dataList, resultData);
        } catch (DuplicateKeyException e) {
            log.info("生产计划[{}]记录数据重复:{}", dto.getInspectionOrderId(), e.getMessage());
            throw new BmosException(LimsResponseCode.EXECUTE_DATA_EXIST);
        } catch (DataIntegrityViolationException e) {
            handleDataTooLong(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * @Author: Ren Jin Guang
     * @Description: 保存拍照上传组件图片
     * @Param: dataList
     * @return:
     * @Date: 2024-07-31 18:12:09
     */
    private void savePicture(List<ExecuteFormData> dataList, Long recordVersionId) {
        List<ExecuteAttachment> pictureList = new ArrayList<>();
        dataList.forEach(item -> {
            if (StrUtil.equals(item.getComponentType(), BasicComponentTypeEnum.PHOTO.getValue()) && StrUtil.isNotEmpty(item.getValue())) {
                if (BooleanUtil.isTrue(item.getEmptyValue())) {
                    return;
                }
                List<ExecuteAttachment> executeAttachments = ExecuteAttachmentConvert.INSTANCE.convertVoList(JsonUtils.parseArray(item.getValue(), AttachmentVO.class), item, recordVersionId);
                pictureList.addAll(executeAttachments);
                String value = String.join(",", CollectionUtils.convertList(executeAttachments, e -> String.valueOf(e.getId())));
                item.setValue(value);
            }
        });
        //保存附件
        executeAttachmentService.saveOrUpdateBatch(pictureList);
    }

    private static void handleDataTooLong(DataIntegrityViolationException e) {
        Throwable cause = e.getCause().getCause();
        if (Objects.equals(cause.getClass(), MysqlDataTruncation.class)) {
            throw new BmosException(LimsResponseCode.CALCULATE_RESULT_TOO_LONG_FOR_COLUMN);
        }
        throw e;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modify(FormDataModifyDTO dto) {
        if (BusinessComponentTypeEnum.CONCLUSION.getValue().equals(dto.getComponentType())) {
            BatchJudgmentDTO judgmentDTO = new BatchJudgmentDTO();
            judgmentDTO.setId(dto.getTaskId());
            judgmentDTO.setJudgedResult(BooleanUtil.toBoolean(dto.getValue()));
            inspectionEntryService.batchUpdateJudgment(Collections.singletonList(judgmentDTO));
        }
        List<ExecuteFormData> affectedData = handleModifyOrUpdate(dto, ExecuteFormDataType.MODIFY);
        // 处理生产信息修订数量
        this.handlePlanModifyCount(dto);
        // 联动检验录入：单字段修改同步到绑定数据点
        tryPropagateModifyToInspectionEntry(dto, affectedData);
        // 复核不通过或审核不通过后，ELN修改组件值需将任务状态置为进行中
        resetTaskStatusIfSampleAuditRejected(dto.getTaskId());
    }

    private void syncLimsConclusion(FormDataBatchSaveDTO dto) {
        if (CollUtil.isEmpty(dto.getItems())) {
            return;
        }
        List<BatchJudgmentDTO> list = new ArrayList<>();
        for (FormDataBatchSaveItemDTO item : dto.getItems()) {
            if (BusinessComponentTypeEnum.CONCLUSION.getValue().equals(item.getComponentType())) {
                BatchJudgmentDTO judgmentDTO = new BatchJudgmentDTO();
                judgmentDTO.setId(dto.getTaskId());
                judgmentDTO.setJudgedResult(BooleanUtil.toBoolean(item.getValue()));
                list.add(judgmentDTO);
            }
        }
        if (CollUtil.isNotEmpty(list)) {
            inspectionEntryService.batchUpdateJudgment(list);
        }
    }

    /**
     * 将ELN保存后的“最终结果”同步到检验录入数据点
     * 最终结果：同一fieldId取操作时间最新的一条（合并本次保存与计算结果）
     */
    private void tryPropagateToInspectionEntry(FormDataBatchSaveDTO dto,
                                               List<ExecuteFormData> dataList,
                                               List<ExecuteFormData> resultData) {
        try {
            List<ExecuteFormData> all = new ArrayList<>();
            if (CollUtil.isNotEmpty(dataList)) {
                all.addAll(dataList);
            }
            if (CollUtil.isNotEmpty(resultData)) {
                all.addAll(resultData);
            }
            if (CollUtil.isEmpty(all)) {
                return;
            }
            // 最新值：同fieldId按operationTime取最后
            Map<Long, ExecuteFormData> latestPerField = all.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(ExecuteFormData::getFieldId,
                            e -> e,
                            (a, b) -> a.getOperationTime().isBefore(b.getOperationTime()) ? b : a));

            Set<Long> fieldIds = latestPerField.keySet();
            if (CollUtil.isEmpty(fieldIds)) {
                return;
            }

            // 先判断来源，再查对应表，避免双查回退
            List<BatchEntryDTO.EntryItemDTO> items = new ArrayList<>();
            if (isStabilitySchemeVersion(dto.getSchemeVersionId())) {
                List<StabilitySchemeDataPoint> boundPoints = stabilitySchemeDataPointMapper.selectList(
                        new LambdaQueryWrapper<StabilitySchemeDataPoint>()
                                .eq(StabilitySchemeDataPoint::getParameterConfigId, dto.getParameterConfigId())
                                .in(StabilitySchemeDataPoint::getFieldId, fieldIds)
                );
                if (CollUtil.isEmpty(boundPoints)) {
                    return;
                }
                for (StabilitySchemeDataPoint p : boundPoints) {
                    ExecuteFormData v = latestPerField.get(p.getFieldId());
                    if (v == null || StrUtil.isBlank(v.getValue())) {
                        continue;
                    }
                    BatchEntryDTO.EntryItemDTO it = new BatchEntryDTO.EntryItemDTO();
                    it.setTaskId(dto.getTaskId());
                    it.setDataPointConfigId(p.getId());
                    it.setPackageId(null);
                    it.setItemConfigId(null);
                    it.setParameterConfigId(dto.getParameterConfigId());
                    it.setDataPointId(p.getDataPointId());
                    it.setDataPointName(p.getName());
                    it.setPointType(p.getPointType());
                    if (DataPointTypeEnum.NUMBER.equals(p.getPointType())) {
                        it.setValueNumber(v.getValue());
                    } else if (DataPointTypeEnum.TIME.equals(p.getPointType())) {
                        it.setValueText(v.getValue());
                        it.setValueNumber(v.getValueExtension());
                    } else {
                        it.setValueText(v.getValue());
                    }
                    items.add(it);
                }
            } else {
                List<InspectionSchemeDataPoint> boundPoints = inspectionSchemeDataPointMapper.selectList(
                        new LambdaQueryWrapper<InspectionSchemeDataPoint>()
                                .eq(InspectionSchemeDataPoint::getParameterConfigId, dto.getParameterConfigId())
                                .in(InspectionSchemeDataPoint::getFieldId, fieldIds)
                );
                if (CollUtil.isEmpty(boundPoints)) {
                    return;
                }
                for (InspectionSchemeDataPoint p : boundPoints) {
                    ExecuteFormData v = latestPerField.get(p.getFieldId());
                    if (v == null || StrUtil.isBlank(v.getValue())) {
                        continue;
                    }
                    BatchEntryDTO.EntryItemDTO it = new BatchEntryDTO.EntryItemDTO();
                    it.setTaskId(dto.getTaskId());
                    it.setDataPointConfigId(p.getId());
                    it.setPackageId(p.getPackageId());
                    it.setItemConfigId(null);
                    it.setParameterConfigId(dto.getParameterConfigId());
                    it.setDataPointId(p.getDataPointId());
                    it.setDataPointName(p.getName());
                    it.setPointType(p.getPointType());
                    if (DataPointTypeEnum.NUMBER.equals(p.getPointType())) {
                        it.setValueNumber(v.getValue());
                    } else if (DataPointTypeEnum.TIME.equals(p.getPointType())) {
                        // TIME：valueText 保存格式化时间，valueNumber 保存秒值（由前端已算好，无需舍入）
                        it.setValueText(v.getValue());
                        it.setValueNumber(v.getValueExtension());
                    } else {
                        // TEXT/OPTION：写入 valueText
                        it.setValueText(v.getValue());
                    }
                    items.add(it);
                }
            }
            if (CollUtil.isNotEmpty(items)) {
                BatchEntryDTO be = new BatchEntryDTO();
                be.setEntryItems(items);
                inspectionEntryService.upsertEntryRecordsFromEln(be);
            }
        } catch (Exception ex) {
            // 保持与检验录入相同的校验与事务一致性：抛出异常回滚
            throw ex;
        }
    }

    /**
     * 将ELN单字段修改同步到检验录入数据点
     */
    private void tryPropagateModifyToInspectionEntry(FormDataModifyDTO dto, List<ExecuteFormData> affectedData) {
        try {
            if ((dto.getFieldId() == null || StrUtil.isBlank(dto.getValue())) && CollUtil.isEmpty(affectedData)) {
                return;
            }
            Set<Long> fieldIds = new HashSet<>();
            if (dto.getFieldId() != null) {
                fieldIds.add(dto.getFieldId());
            }
            if (CollUtil.isNotEmpty(affectedData)) {
                fieldIds.addAll(CollectionUtils.convertSet(affectedData, ExecuteFormData::getFieldId));
            }
            if (CollUtil.isEmpty(fieldIds)) {
                return;
            }

            // 构建 latestPerField
            Map<Long, ExecuteFormData> latestPerField = new HashMap<>();
            if (CollUtil.isNotEmpty(affectedData)) {
                for (ExecuteFormData d : affectedData) {
                    if (d == null || d.getFieldId() == null || StrUtil.isBlank(d.getValue())) {
                        continue;
                    }
                    ExecuteFormData existing = latestPerField.get(d.getFieldId());
                    if (existing == null || (existing.getOperationTime() != null
                            && d.getOperationTime() != null
                            && existing.getOperationTime().isBefore(d.getOperationTime()))) {
                        latestPerField.put(d.getFieldId(), d);
                    }
                }
            }
            if (dto.getFieldId() != null && !latestPerField.containsKey(dto.getFieldId()) && StrUtil.isNotBlank(dto.getValue())) {
                ExecuteFormData ad = new ExecuteFormData();
                ad.setFieldId(dto.getFieldId());
                ad.setValue(dto.getValue());
                ad.setValueExtension(dto.getValueExtension());
                ad.setOperationTime(LocalDateTime.now());
                latestPerField.put(dto.getFieldId(), ad);
            }

            // 先判断来源，再查对应表，避免双查回退
            List<BatchEntryDTO.EntryItemDTO> items = new ArrayList<>();
            if (isStabilitySchemeVersion(dto.getSchemeVersionId())) {
                List<StabilitySchemeDataPoint> boundPoints = stabilitySchemeDataPointMapper.selectList(
                        new LambdaQueryWrapper<StabilitySchemeDataPoint>()
                                .eq(StabilitySchemeDataPoint::getParameterConfigId, dto.getParameterConfigId())
                                .in(StabilitySchemeDataPoint::getFieldId, fieldIds)
                );
                if (CollUtil.isEmpty(boundPoints)) {
                    return;
                }
                for (StabilitySchemeDataPoint p : boundPoints) {
                    ExecuteFormData v = latestPerField.get(p.getFieldId());
                    if (v == null || StrUtil.isBlank(v.getValue())) {
                        continue;
                    }
                    BatchEntryDTO.EntryItemDTO it = new BatchEntryDTO.EntryItemDTO();
                    it.setTaskId(dto.getTaskId());
                    it.setDataPointConfigId(p.getId());
                    it.setPackageId(null);
                    it.setItemConfigId(null);
                    it.setParameterConfigId(dto.getParameterConfigId());
                    it.setDataPointId(p.getDataPointId());
                    it.setDataPointName(p.getName());
                    it.setPointType(p.getPointType());
                    if (DataPointTypeEnum.NUMBER.equals(p.getPointType())) {
                        it.setValueNumber(v.getValue());
                    } else if (DataPointTypeEnum.TIME.equals(p.getPointType())) {
                        it.setValueText(v.getValue());
                        it.setValueNumber(v.getValueExtension());
                    } else {
                        it.setValueText(v.getValue());
                    }
                    items.add(it);
                }
            } else {
                List<InspectionSchemeDataPoint> boundPoints = inspectionSchemeDataPointMapper.selectList(
                        new LambdaQueryWrapper<InspectionSchemeDataPoint>()
                                .eq(InspectionSchemeDataPoint::getParameterConfigId, dto.getParameterConfigId())
                                .in(InspectionSchemeDataPoint::getFieldId, fieldIds)
                );
                if (CollUtil.isEmpty(boundPoints)) {
                    return;
                }
                for (InspectionSchemeDataPoint p : boundPoints) {
                    ExecuteFormData v = latestPerField.get(p.getFieldId());
                    if (v == null || StrUtil.isBlank(v.getValue())) {
                        continue;
                    }
                    BatchEntryDTO.EntryItemDTO it = new BatchEntryDTO.EntryItemDTO();
                    it.setTaskId(dto.getTaskId());
                    it.setDataPointConfigId(p.getId());
                    it.setPackageId(p.getPackageId());
                    it.setItemConfigId(null);
                    it.setParameterConfigId(dto.getParameterConfigId());
                    it.setDataPointId(p.getDataPointId());
                    it.setDataPointName(p.getName());
                    it.setPointType(p.getPointType());
                    if (DataPointTypeEnum.NUMBER.equals(p.getPointType())) {
                        it.setValueNumber(v.getValue());
                    } else if (DataPointTypeEnum.TIME.equals(p.getPointType())) {
                        // TIME：valueText 保存格式化时间，valueNumber 保存秒值（由前端已算好，无需舍入）
                        it.setValueText(v.getValue());
                        it.setValueNumber(v.getValueExtension());
                    } else {
                        it.setValueText(v.getValue());
                    }
                    items.add(it);
                }
            }
            if (CollUtil.isNotEmpty(items)) {
                BatchEntryDTO be = new BatchEntryDTO();
                be.setEntryItems(items);
                inspectionEntryService.upsertEntryRecordsFromEln(be);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * 根据方案版本 ID 判断是否为稳定性任务（查一次即可，无需双查回退）
     */
    private boolean isStabilitySchemeVersion(Long schemeVersionId) {
        return schemeVersionId != null
                && stabilitySchemeVersionMapper.selectById(schemeVersionId) != null;
    }

    private void handlePlanModifyCount(FormDataModifyDTO dto) {
        InspectionOrder order = inspectionOrderMapper.selectById(dto.getInspectionOrderId());
        Integer count = executeFormDataMapper.countModifyFieldByPlanId(dto.getInspectionOrderId());
        if (Objects.equals(order.getModifyCount(), count)) {
            return;
        }
        // 如果count有变动
        order.setModifyCount(count);
        inspectionOrderMapper.updateById(order);
        // 获取生产信息组件修订数量组件值
        InspectionSchemeParameter inspectionSchemeParameter = inspectionSchemeParameterMapper.selectById(dto.getParameterConfigId());
        List<ExecuteFormData> insertList = generateCurrentPlanModifyCountFieldFormData(inspectionSchemeParameter, order);
        saveAndCalculateResults(insertList, inspectionSchemeParameter, order);
    }

    private void handlePlanModifyCount(Long inspectOrderId, InspectionSchemeParameter inspectionSchemeParameter) {
        InspectionOrder order = inspectionOrderMapper.selectById(inspectOrderId);
        Integer count = executeFormDataMapper.countModifyFieldByPlanId(inspectOrderId);
        if (Objects.equals(order.getModifyCount(), count)) {
            return;
        }
        // 如果count有变动
        order.setModifyCount(count);
        inspectionOrderMapper.updateById(order);
        // 获取生产信息组件修订数量组件值
        List<ExecuteFormData> insertList = generateCurrentPlanModifyCountFieldFormData(inspectionSchemeParameter, order);
        saveAndCalculateResults(insertList, inspectionSchemeParameter, order);
    }

    /**
     * 组装当前生产批次下已存的修订数量组件列表
     *
     * @param parameter   修订数据
     * @param order 检验单
     * @return
     */
    private List<ExecuteFormData> generateCurrentPlanModifyCountFieldFormData(InspectionSchemeParameter parameter, InspectionOrder order) {
        List<ExecuteFormData> executeFormData =
                executeFormDataMapper.selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                        .eq(ExecuteFormData::getInspectionOrderId, order.getId())
                        .eq(ExecuteFormData::getComponentType,
                                BusinessComponentTypeEnum.BUSINESS_PRODUCT_INFO_REVISION_NUMBER.getValue()));
        // 分析项MAP
        List<ExecuteFormData> insertList = new ArrayList<>();
        if (CollUtil.isEmpty(executeFormData)) {
            return insertList;
        }

        Set<Long> existedFieldIds = new HashSet<>();
        // 处理当前页的数据
        for (ExecuteFormData data : executeFormData) {
            if (!existedFieldIds.contains(data.getFieldId())) {
                existedFieldIds.add(data.getFieldId());
                ExecuteFormData formData = ExecuteFormDataConverter.INSTANCE.convert(order,parameter);
                formData.setId(IdUtils.getSnowflake());
                formData.setFieldId(data.getFieldId());
                formData.setComponentType(data.getComponentType());
                formData.setValue(String.valueOf(order.getModifyCount()));
                insertList.add(formData);
            }
        }
        return insertList;
    }


    @Override
    public List<ExecuteFormData> calculateData(List<ExecuteFormData> saveData, CalculateDataQueryDTO query) {
        // 根据版本id获取图
        Graph<Long> graph = batchRecordComponentService.getGraph(query.getRecordVersionId());
        Set<Long> saveFields = CollectionUtils.convertSet(saveData, ExecuteFormData::getFieldId);
        // 获取该版本图中所有数据单元格
        Set<Long> dataElementsSet = graph.getDataElements();
        saveFields.retainAll(dataElementsSet);
        // 如果要保存的数据，都不在图元素中，直接返回
        if (CollUtil.isEmpty(saveFields)) {
            return new ArrayList<>();
        }
        LocalDateTime operateTime = LocalDateTime.now();
        saveData.forEach(e -> e.setOperationTime(operateTime));
        // 查询 图中所有元素中结果组件的公式配置
        Set<Long> allFields = new HashSet<>(graph.getAllElements());
        // 查询需要计算单元格的公式
        Map<Long, BatchRecordComponent> batchRecordComponentMap = batchRecordComponentService
                .selectByRecordVersionIdAndFields(query.getRecordVersionId(), allFields, true)
                .stream()
                .collect(Collectors.toMap(BatchRecordComponent::getFieldId, Function.identity(), (t1, t2) -> t1));

        return calculateAndHandle(CalculateBasicContext.builder()
                .saveData(saveData)
                .query(query)
                .graph(graph)
                .batchRecordComponentMap(batchRecordComponentMap)
                .build());
    }


    /**
     * 计算基础上下文
     */
    @Builder
    @Getter
    public static class CalculateBasicContext {
        private List<ExecuteFormData> saveData;

        private Graph<Long> graph;

        private CalculateDataQueryDTO query;

        private Map<Long, BatchRecordComponent> batchRecordComponentMap;

    }

    private List<ExecuteFormData> calculateAndHandle(CalculateBasicContext basicContext) {
        // 查询图中所有字段的值 该字段与最新保存的字段需合并，合并
        // graph.getAllElements()根据图中所有字段，查询数据
        boolean isStability = basicContext.query.getSchemeVersionId() != null
                && stabilitySchemeVersionMapper.selectById(basicContext.query.getSchemeVersionId()) != null;
        List<FieldConfigVO> fieldConfigs = isStability
                ? stabilitySchemeParameterMapper.getFieldsConfig(basicContext.query, basicContext.graph.getAllElements())
                : inspectionSchemeParameterMapper.getFieldsConfig(basicContext.query, basicContext.graph.getAllElements());

        Map<Long, List<FieldConfigVO>> fieldMultiMap = CollectionUtils.convertMultiMap(fieldConfigs,
                FieldConfigVO::getFieldId);
        //查询生产中在图中的所有记录项产生的数据R
        Set<Long> recordItemIds = CollectionUtils.convertSet(fieldConfigs, FieldConfigVO::getRecordItemId);
        List<ExecuteFormData> needData =
                CollUtil.isEmpty(recordItemIds) ? new ArrayList<>() :
                        executeFormDataMapper.selectByProductPlanIdAndItemIdsWithDiscard(basicContext.query.getInspectionOrderId(), recordItemIds);
        Set<Long> fieldsInDB = CollectionUtils.convertSet(needData, ExecuteFormData::getFieldId);
        // 新的数据需要取最新的
        needData.addAll(basicContext.saveData);

        List<ExecuteFormData> calculateDataList = this.calculateRelationData(basicContext, needData,
                fieldMultiMap);

        LocalDateTime operationTime = LocalDateTime.now();
        List<ExecuteFormData> result =
                calculateDataList.stream().map(e -> ExecuteFormDataConverter.INSTANCE.buildFormData(basicContext.query, fieldsInDB,
                        operationTime, e)).collect(Collectors.toList());
        return result;
    }

    /**
     * @param valueMap key:节点; value:引用该节点的节点列表
     * @param context  计算上下文
     * @return 计算结果
     */
    public List<ExecuteFormData> calculateNodeValues(Map<Long, List<Long>> valueMap,
                                                     CalculateContext context) {

        // 初始化入度表 各节点入度初始为0
        Map<Long, Integer> inDegree = new HashMap<>();
        for (Long node : valueMap.keySet()) {
            inDegree.put(node, 0);
        }
        Map<String, ExecuteFormData> dbDataMap = new HashMap<>(context.fullPathMap);
        // 计算各节点入度并更新
        for (List<Long> adjNodes : valueMap.values()) {
            for (Long adjNode : adjNodes) {
                inDegree.put(adjNode, inDegree.getOrDefault(adjNode, 0) + 1);
            }
        }

        // 初始化队列，将所有入度为0的节点加入队列
        Queue<Long> queue = new LinkedList<>();
        for (Map.Entry<Long, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            Long currentNode = queue.poll();
            // 更新所有引用当前节点的值
            for (Long adjNode : valueMap.getOrDefault(currentNode, new ArrayList<>())) {
                // 获取组件公式类型并计算
                BatchRecordComponent batchRecordComponent = context.componentMap.get(adjNode);
                if (batchRecordComponent == null) {
                    continue;
                }
                List<FieldConfigVO> fieldConfigVOS = context.getFieldConfigMap().get(adjNode);
                // 处理同一field在不同记录页上的值
                if (CollUtil.isNotEmpty(fieldConfigVOS)) {
                    handleStepsFieldValue(context, fieldConfigVOS, batchRecordComponent);
                }
                // 更新入度且当入度更新为0时入队
                inDegree.put(adjNode, inDegree.get(adjNode) - 1);
                if (inDegree.get(adjNode) == 0) {
                    queue.add(adjNode);
                }
            }
        }
        List<ExecuteFormData> result = new ArrayList<>();
        for (Map.Entry<String, ExecuteFormData> entry : context.getFullPathMap().entrySet()) {
            ExecuteFormData value = entry.getValue();
            if (StrUtil.isNotEmpty(value.getValue())
                    && !Objects.equals(dbDataMap.getOrDefault(getFullPath(value), new ExecuteFormData()).getValue(), value.getValue())) {
                result.add(value);
            }
        }
        return result;
    }

    /**
     * 处理同一组件在不同步骤上的计算结果
     * 含不复用、复制、换班
     *
     * @param context
     * @param fieldConfigVOS
     * @param batchRecordComponent
     */
    private void handleStepsFieldValue(CalculateContext context, List<FieldConfigVO> fieldConfigVOS,
                                       BatchRecordComponent batchRecordComponent) {
        for (FieldConfigVO fieldConfigVO : fieldConfigVOS) {
            handleRecordFieldValue(context, batchRecordComponent, fieldConfigVO);
        }
    }

    /**
     * 处理计算缓存field的值
     *
     * @param context
     * @param batchRecordComponent
     */
    private void handleRecordFieldValue(CalculateContext context,
                                        BatchRecordComponent batchRecordComponent, FieldConfigVO fieldConfigVO) {
        ExecuteFormData tempResult = getComponentCalculateResult(context, batchRecordComponent, fieldConfigVO);
        if (tempResult != null) {
            tempResult.setParameterId(fieldConfigVO.getInspectParameterId());
            String path = getFullPath(tempResult);
            ExecuteFormData existed = context.getFullPathMap().get(path);
            // 过滤出原先就有的值
            if (existed == null || !Objects.equals(existed.getValue(), tempResult.getValue())) {
                context.getFullPathMap().put(path, tempResult);
            }
            // 更新组件最新值
            context.getFieldMap().put(tempResult.getFieldId(), tempResult);
        }
    }

    @Builder
    @Getter
    public static class CalculateContext {
        // 计算错误值
        private String recordErrorDataValue;
        // 空值
        private String emptyData;
        // 数据分组map field
        private Map<Long, List<ExecuteFormData>> groupMap;
        // 签名公式时间格式
        private String signatureTimeFormat;
        // field配置map 区分步骤
        private Map<Long, List<FieldConfigVO>> fieldConfigMap;
        // 全路径值map 区分步骤和版本
        private Map<String, ExecuteFormData> fullPathMap;
        // 组件配置 用于取公式配置
        private Map<Long, BatchRecordComponent> componentMap;
        // 组件最新值 不区分复用复制页
        private Map<Long, ExecuteFormData> fieldMap;
    }

    /**
     * @param context              计算上下文
     * @param batchRecordComponent 当前计算的组件
     * @return
     */
    private ExecuteFormData getComponentCalculateResult(CalculateContext context, BatchRecordComponent batchRecordComponent,
                                                        FieldConfigVO fieldConfigVO) {
        ComponentFormulaTypeEnum componentType =
                ComponentFormulaTypeEnum.getEnumByValue(batchRecordComponent.getFormulaType());

        List<FormulaFieldDTO> formulaFieldList = JsonUtils.parseArray(
                batchRecordComponent.getFormulaField(),
                FormulaFieldDTO.class
        );
        // 取出节点计算所需值
        List<ExecuteFormData> fieldsValue;
        ComponentFormulaConfig formulaConfig = batchRecordComponent.getFormulaConfig();
        if (formulaConfig != null &&
                Objects.equals(formulaConfig.getValueTakeType(), FormulaValueTakeTypeEnum.ALL_EFFECTIVE.getValue())) {
            fieldsValue = getFieldsAllEffectiveValue(formulaFieldList, context);
        } else {
            fieldsValue = getFieldsValue(formulaFieldList, context);
        }
        List<String> keyList = CollectionUtils.convertList(formulaFieldList, FormulaFieldDTO::getKey);
        CalculateResult calculateResult = new CalculateResult();
        boolean allEmpty = false;
        try {
            fieldsValue = fieldsValue.stream()
                    .filter(item -> item != null && StrUtil.isNotEmpty(item.getValue()))
                    .collect(Collectors.toList());
            // 完全无参数值则返回不计算
            if (CollUtil.isEmpty(fieldsValue)) {
                return null;
            }
            allEmpty = fieldsValue.stream().allMatch(e -> BooleanUtil.isTrue(e.getEmptyValue())
                    || StrUtil.equals(e.getValue(), context.getEmptyData()));
            // 空值、空字符串、参数配置的空值都赋值为公式的默认值
            // 复制一份使用 不修改原值
            List<CalculateParam> tempValueList = BeanUtil.copyToList(fieldsValue, CalculateParam.class);
            for (CalculateParam e : tempValueList) {
                e.setTimeFormat(context.getSignatureTimeFormat());
                if (BooleanUtil.isTrue(e.getEmptyValue()) || (StrUtil.equals(e.getValue(), context.getEmptyData()))
                        || StrUtil.isEmpty(e.getValue())) {
                    e.setValue(componentType.getDefaultEmptyValue());
                }
                e.setEmptyValue(e.getEmptyValue());
            }
            calculateResult = allEmpty ? calculateResult.emptyValue(getEmptyResult(batchRecordComponent,
                    context.getEmptyData())) : componentType.getFunction().apply(batchRecordComponent, keyList,
                    tempValueList, expressionCalculator::evaluate);
        } catch (Exception e) {
            log.error("公式计算错误", e);
            // 参数全为配置空值 结果为空值
            if (allEmpty) {
                calculateResult.emptyValue(getEmptyResult(batchRecordComponent, context.getEmptyData()));
            } else {
                calculateResult.calculateError(context.getRecordErrorDataValue());
            }
        }
        if (calculateResult == null || StrUtil.isEmpty(calculateResult.getValue())) {
            return null;
        }
        ExecuteFormData result = new ExecuteFormData();
        result.setValue(calculateResult.getValue());
        result.setExtInfo(calculateResult.getExtInfo());
        result.setValueExtension(calculateResult.getExtInfo());
        result.setFieldId(batchRecordComponent.getFieldId());
        result.setOperationUser(SysUserHolder.getUser().getUserId());
        result.setOperationTime(LocalDateTime.now());
        result.setRecordItemId(fieldConfigVO.getRecordItemId());
        result.setComponentType(batchRecordComponent.getComponentType());
        result.setEmptyValue(calculateResult.getEmptyValue());
        return result;
    }

    private String getEmptyResult(BatchRecordComponent batchRecordComponent, String emptyData) {
        if (StrUtil.equals(batchRecordComponent.getComponentType(), BasicComponentTypeEnum.CHECKBOX.getValue())) {
            return JsonUtils.toJsonString(Collections.singletonList(emptyData));
        }
        return emptyData;
    }

    /**
     * 获取所有有效值 如同一个组件存在记录复制记录非复用等多个数据来源 全部去除
     *
     * @param fields
     * @param context
     * @return
     */
    private List<ExecuteFormData> getFieldsAllEffectiveValue(List<FormulaFieldDTO> fields,
                                                             CalculateContext context) {
        return fields.stream().map(e -> {
            FieldConfigVO fieldConfigVO = CollUtil.getFirst(context.getFieldConfigMap().get(e.getFieldId()));
            if (fieldConfigVO == null) {
                return null;
            }
            // 在所有有效取值方式下 不区分当前页和其他页 取出所有复制或复用页当前组件的有效值
            List<ExecuteFormData> formDataList = context.getGroupMap().getOrDefault(e.getFieldId(), new ArrayList<>());
            return CollUtil.getLast(formDataList);
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 常规取值 只取某个组件最新值
     *
     * @param fields
     * @param context
     * @return
     */
    private List<ExecuteFormData> getFieldsValue(List<FormulaFieldDTO> fields, CalculateContext context) {
        return fields.stream().map(e -> {
            FieldConfigVO fieldConfigVO = CollUtil.getFirst(context.getFieldConfigMap().get(e.getFieldId()));
            if (fieldConfigVO == null) {
                return null;
            }
            return context.getFieldMap().get(e.getFieldId());
        }).collect(Collectors.toList());
    }


    /**
     * @param basicContext    基础上下文
     * @param currentDataList 保存值与查询值列表合集
     * @param fieldConfigMap  组件配置map
     * @return
     */
    private List<ExecuteFormData> calculateRelationData(CalculateBasicContext basicContext,
                                                        List<ExecuteFormData> currentDataList,
                                                        Map<Long, List<FieldConfigVO>> fieldConfigMap) {
        Map<Long, ExecuteFormData> fieldDataMap = currentDataList.stream()
                .collect(Collectors.toMap(
                        ExecuteFormData::getFieldId, // 按 id 分组
                        e -> e, // 当前元素
                        (existing, replacement) -> existing.getOperationTime().isBefore(replacement.getOperationTime()) ? replacement : existing // 保留操作时间最新的值
                ));
        // 此处按时间升序排 取值取最新 更新时往list后追加元素 因为签名公式及后续公式会用到历史值 所以需要将历史值也往下传
        Map<Long, List<ExecuteFormData>> groupMap = getAllDataMap(currentDataList);
        // 初始化全路径值map 用于确定唯一值及保存结果
        Map<String, ExecuteFormData> fullPathMap = initFullPathMap(currentDataList);
        // 图引用关系
        Map<Long, List<Long>> valueMap = basicContext.graph.getValueMap();
        // 公式计算错误时的错误值
        String recordErrorDataValue = platformParameterClientImpl.getValueByCode(RECORD_ERROR_DATA_CODE);
        String emptyData = platformParameterClientImpl.getValueByCode(RECORD_EMPTY_DATA);
        String signatureTimeFormat = platformParameterClientImpl.getValueByCode(BusinessParameterCodeConstants.PLATFORM_SIGNATURE_TIME_FORMAT);
        // 组装计算上下文
        CalculateContext context = CalculateContext.builder()
                .recordErrorDataValue(recordErrorDataValue)
                .emptyData(emptyData)
                .fieldConfigMap(fieldConfigMap)
                .fullPathMap(fullPathMap).groupMap(groupMap)
                .componentMap(basicContext.batchRecordComponentMap)
                .fieldMap(fieldDataMap)
                .signatureTimeFormat(signatureTimeFormat)
                .build();
        // 计算每个节点的值
        return calculateNodeValues(valueMap, context);
    }

    @NotNull
    private static Map<String, ExecuteFormData> initFullPathMap(List<ExecuteFormData> currentDataList) {
        Map<String, ExecuteFormData> fullPathMap = new HashMap<>();
        currentDataList.forEach(e -> {
            String path = getFullPath(e);
            ExecuteFormData executeFormData = fullPathMap.get(path);
            if (executeFormData != null) {
                if (e.getOperationTime().isAfter(executeFormData.getOperationTime())) {
                    fullPathMap.put(path, e);
                }
            } else {
                fullPathMap.put(path, e);
            }
        });
        return fullPathMap;
    }

    @NotNull
    private static String getFullPath(ExecuteFormData e) {
        return e.getFieldId() + StrUtil.DASHED + e.getItemConfigId();
    }

    @NotNull
    private static Map<Long, List<ExecuteFormData>> getAllDataMap(List<ExecuteFormData> currentDataList) {
        return currentDataList.stream().collect(Collectors.groupingBy(ExecuteFormData::getFieldId, Collectors.collectingAndThen(Collectors.toList(), list -> {
            list.sort(Comparator.comparing(ExecuteFormData::getOperationTime));
            return list;
        })));
    }

    /**
     * 在无数值更新保存的情况下计算当前复制的记录页的数据
     *
     * @param query
     * @return
     */
    private List<ExecuteFormData> calculateDataWithNoParams(CalculateDataQueryDTO query) {
        // 根据版本id获取图
        Graph<Long> graph = batchRecordComponentService.getGraph(query.getRecordVersionId());
        // 查询 图中所有元素中结果组件的公式配置
        Set<Long> allFields = new HashSet<>(graph.getAllElements());
        if (CollectionUtil.isEmpty(allFields)) {
            return new ArrayList<>();
        }
        // 查询需要计算单元格的公式
        Map<Long, BatchRecordComponent> batchRecordComponentMap = batchRecordComponentService
                .selectByRecordVersionIdAndFields(query.getRecordVersionId(), allFields, true)
                .stream()
                .collect(Collectors.toMap(BatchRecordComponent::getFieldId, Function.identity(), (t1, t2) -> t1));
        List<ExecuteFormData> executeFormData = calculateAndHandle(CalculateBasicContext.builder()
                .saveData(new ArrayList<>())
                .query(query)
                .graph(graph)
                .batchRecordComponentMap(batchRecordComponentMap)
                .build());
        return executeFormData;
    }

    @Override
    public String getServerTime() {
        return LocalDateTimeUtil.formatNormal(LocalDateTime.now());
    }

    @Override
    public void insertBatch(List<ExecuteFormData> results) {
        executeFormDataMapper.insertBatch(results);
    }


    private Long selectMaxRev(Long productPlanId, Set<Long> fields) {
        Long rev = executeFormDataMapper.selectMaxRev(productPlanId, fields);
        return rev == null ? 0L : rev + 1L;
    }


    private List<ExecuteFormData> saveAndCalculateResults(List<ExecuteFormData> results, InspectionSchemeParameter inspectionSchemeParameter, InspectionOrder order, boolean filterNull) {
        // 过滤值为空的 不进行更新
        if (filterNull) {
            results = results.stream().filter(item -> StrUtil.isNotEmpty(item.getValue())).collect(Collectors.toList());
        }
        if (CollUtil.isEmpty(results)) {
            return new ArrayList<>();
        }
        CalculateDataQueryDTO queryDTO = ExecuteFormDataConverter.INSTANCE.convert2CalculateQueryDto(order,inspectionSchemeParameter);
        // 填充表单数据 (操作类型班组信息) 处理业务组件时间日期格式
        results  = formDataHandleService.fillFormDataAndFilter(FormDataFilterDTO.builder()
                .dataList(results)
                .parameterConfigId(inspectionSchemeParameter.getId())
                .build());
        executeFormDataMapper.insertBatch(results);
        List<ExecuteFormData> resultData =
                this.calculateData(results, queryDTO);
        List<ExecuteFormData> filter = resultData.stream().filter(e -> StrUtil.isNotEmpty(e.getValue())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(filter)) {
            Long buildRev = this.selectMaxRev(order.getId(), CollectionUtils.convertSet(filter, ExecuteFormData::getFieldId));
            filter.forEach(e -> {
                e.setRev(buildRev);
            });
            this.insertBatch(filter);
        }
        return filter;
    }

    private List<ExecuteFormData> saveAndCalculateResults(List<ExecuteFormData> results, InspectionSchemeParameter  parameterConfig, InspectionOrder order) {
        return saveAndCalculateResults(results, parameterConfig, order, true);
    }

    /**
     * 样品审核不通过后，ELN修改组件值需将任务状态置为待复核
     * @param taskId 任务ID
     */
    private void resetTaskStatusIfSampleAuditRejected(Long taskId) {
        if (taskId == null) {
            return;
        }
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            return;
        }

        // 检查任务是否处于复核不通过或审核不通过状态
        TaskStatusEnum currentStatus = task.getStatus();
        if (!TaskStatusEnum.REVIEW_REJECTED.equals(currentStatus)
                && !TaskStatusEnum.SAMPLE_AUDIT_REJECTED.equals(currentStatus)) {
            return;
        }

        // 只更新任务状态为进行中，不影响其他字段
        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Task::getId, taskId)
                .set(Task::getStatus, TaskStatusEnum.IN_PROGRESS);
        taskMapper.update(null, updateWrapper);

        // 记录任务状态变更历史
        com.bmos.lims2.server.task.entity.TaskStatusHistory history = new com.bmos.lims2.server.task.entity.TaskStatusHistory();
        history.setTaskId(taskId);
        history.setOperationType(TaskOperationTypeEnum.ELN_DATA_MODIFY);
        history.setFromStatus(currentStatus.getValue());
        history.setToStatus(TaskStatusEnum.IN_PROGRESS.getValue());
        history.setOperatorId(SysUserHolder.getUser().getUserId());
        history.setOperateTime(java.time.LocalDateTime.now());
        history.setComment("ELN修改数据，任务状态从" + currentStatus.getName() + "变更为进行中");
        taskStatusHistoryMapper.insert(history);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FormDataUpdateDTO dto) {
        handleModifyOrUpdate(ExecuteFormDataConverter.INSTANCE.convertToModifyDTO(dto), ExecuteFormDataType.UPDATE);
    }


    /**
     * 处理组件数据值修订或更新
     *
     * @param dto
     * @param type 数据操作类型 修订/更新
     */
    private List<ExecuteFormData> handleModifyOrUpdate(FormDataModifyDTO dto, ExecuteFormDataType type) {
        RLock lock = redissonClient.getLock(String.format(RedissionKeyConstant.EXECUTE_EXPRESS,
                dto.getInspectionOrderId()));
        boolean lockResult = lock.tryLock();
        if (!lockResult) {
            throw new BmosException(LimsResponseCode.PROCEDURE_EXPRESS_LOCKED);
        }
        try {
            ExecuteFormData data = ExecuteFormDataConverter.INSTANCE.convert(dto);
            data.setOperationType(type.getValue());
            data.setExtInfo(dto.getValueExtension());
            List<ExecuteFormData> calculateDataList = calculateData(CollUtil.toList(data),
                    ExecuteFormDataConverter.INSTANCE.convertQuery(dto));
            Set<Long> fields = CollectionUtils.convertSet(calculateDataList, ExecuteFormData::getFieldId);
            fields.add(data.getFieldId());
            Long rev = executeFormDataMapper.selectMaxRev(dto.getInspectionOrderId(), fields);
            long maxRev = rev == null ? 0L : rev + 1L;
            calculateDataList.forEach(e -> e.setRev(maxRev));
            executeFormDataMapper.insertBatch(calculateDataList);
            data.setRev(maxRev);
            if (ObjectUtil.isNotNull(data)) {
                savePicture(Collections.singletonList(data), dto.getRecordVersionId());
            }
            executeFormDataMapper.insert(data);
            List<ExecuteFormData> affected = new ArrayList<>(calculateDataList);
            affected.add(data);
            return affected;
        } catch (DuplicateKeyException e) {
            log.info("检验ELN[{}]记录数据重复:{}", dto.getInspectionOrderId(), e.getMessage());
            throw new BmosException(LimsResponseCode.EXECUTE_DATA_EXIST);
        } catch (DataIntegrityViolationException e) {
            handleDataTooLong(e);
        } finally {
            lock.unlock();
        }
        return Collections.emptyList();
    }

    @Override
    public AttachmentVO upload(MultipartFile file) {
        try {
            String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            File files = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, suffix);
            file.transferTo(files);
            String key = IdUtils.getSnowflakeStr() + "_" + System.currentTimeMillis();
            String bucketName = minioFileClient.getBucketName(MinioBucket.ELN_RECORD);
            String uploadPatch = minioFileClient.uploadFile(MinioBucket.ELN_RECORD, files, String.format("/%s" + suffix, key));
            return AttachmentVO.builder()
                    .id(IdUtils.getSnowflake())
                    .type(suffix)
                    .createBy(SysUserHolder.getUser().getUserId())
                    .createTime(LocalDateTime.now())
                    .path(bucketName + uploadPatch)
                    .build();
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new BmosException(LimsResponseCode.ATTACHMENT_FILE_ERROR);
        }
    }


    @Override
    public List<FormDataItemVO> getCalculationPreview(FormDataBatchSaveDTO dto) {
        if (CollUtil.isEmpty(dto.getItems())) {
            return new ArrayList<>();
        }
        List<ExecuteFormData> resultData = calculateData(
                ExecuteFormDataConverter.INSTANCE.convert(dto),
                ExecuteFormDataConverter.INSTANCE.convertQuery(dto));
        if (CollUtil.isEmpty(resultData)) {
            return new ArrayList<>();
        }
        return resultData.stream().filter(e->{
            return Objects.equals(e.getRecordId(), dto.getRecordId()) &&
                    Objects.equals(e.getRecordItemId(), dto.getRecordItemId());
        }).map(ExecuteFormDataConverter.INSTANCE::convert2FormDataItemVO).collect(Collectors.toList());
    }

    @Override
    public List<FormDataVO> getFieldList(FormDataListQueryDTO dto) {
        List<ExecuteFormData> dataList = executeFormDataMapper.selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getInspectionOrderId, dto.getInspectionOrderId())
                .eq(ExecuteFormData::getParameterConfigId, dto.getParameterConfigId())
                .eq(ExecuteFormData::getFieldId, dto.getFieldId())
                .eq(ExecuteFormData::getTaskId, dto.getTaskId())
                .eq(Boolean.TRUE.equals(dto.getDiscard()), ExecuteFormData::getDiscard, true)
                .eq(Boolean.FALSE.equals(dto.getDiscard()) || dto.getDiscard() == null, ExecuteFormData::getDiscard, false)
                .orderByAsc(ExecuteFormData::getOperationTime));
        return dataList.stream().map(e -> {
            FormDataVO vo = new FormDataVO();
            vo.setFieldId(e.getFieldId());
            vo.setValue(e.getValue());
            vo.setValueExtension(e.getValueExtension());
            vo.setOperationType(e.getOperationType());
            vo.setOperationUser(e.getOperationUser());
            vo.setOperationTime(e.getOperationTime());
            vo.setReviewUser(e.getReviewUser());
            vo.setReviewTime(e.getReviewTime());
            vo.setSystemCreate(e.getSystemCreate());
            vo.setRemark(e.getRemark());
            vo.setEmptyValue(e.getEmptyValue());
            vo.setTaskId(e.getTaskId());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<FormDataItemVO> getRecordItemLatestData(RecordItemLatestDataQueryDTO dto) {
        List<ExecuteFormData> dataList = executeFormDataMapper.selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getInspectionOrderId, dto.getInspectionOrderId())
                .eq(ExecuteFormData::getParameterConfigId, dto.getParameterConfigId())
                .eq(ExecuteFormData::getRecordItemId, dto.getRecordItemId())
                .eq(ExecuteFormData::getTaskId, dto.getTaskId())
                .eqIfPresent(ExecuteFormData::getDiscard, dto.getDiscard())
                .inIfPresent(ExecuteFormData::getFieldId, dto.getFieldIdList())
                .orderByDesc(ExecuteFormData::getFieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
        Map<Long, ExecuteFormData> latestPerField = new HashMap<>();
        for (ExecuteFormData e : dataList) {
            if (!latestPerField.containsKey(e.getFieldId())) {
                latestPerField.put(e.getFieldId(), e);
            }
        }
        String emptyData = platformParameterClientImpl.getValueByCode(RECORD_EMPTY_DATA);
        List<ExecuteFormData> executeFormData = CollectionUtils.filterList(dataList, item ->
                StrUtil.equals(item.getComponentType(), BasicComponentTypeEnum.PHOTO.getValue()) &&
                        !item.getValue().equals(emptyData));
        List<AttachmentVO> attachmentVOList = executeAttachmentService.getListByIdList(executeFormData);
        return ExecuteFormDataConverter.INSTANCE.filterLatestAndConvert(dataList,attachmentVOList,emptyData);
    }

    @Override
    public void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, BusinessDataHandleBaseDTO dto) {
        saveResultsAndHandleRelationComponentData(results, dto, true);
    }

    @Override
    public void  saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, BusinessDataHandleBaseDTO dto, boolean filterNull) {
        this.saveResultsAndHandleRelationComponentData(results, dto.getInspectionOrderId(), dto.getParameterConfigId(), filterNull);
    }


    @Override
    public void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, Long inspectionOrderId,
                                                          Long parameterConfigId) {
        saveResultsAndHandleRelationComponentData(results, inspectionOrderId, parameterConfigId, true);
    }



    @Override
    public void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, Long inspectionOrderId,
                                                          Long parameterConfigId, boolean filterNull) {
        RLock lock = redissonClient.getLock(String.format(RedissionKeyConstant.EXECUTE_EXPRESS,
                inspectionOrderId));
        boolean lockResult = lock.tryLock();
        if (!lockResult) {
            throw new BmosException(LimsResponseCode.PROCEDURE_EXPRESS_LOCKED);
        }
        // 先查检验单以确定来源，再按来源查对应的分析项配置表
        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(inspectionOrderId);
        InspectionSchemeParameter inspectionSchemeParameter;
        if (InspectionOrderSourceEnum.STABILITY.equals(inspectionOrder.getSchemeSource())) {
            com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeParameter sp =
                    stabilitySchemeParameterMapper.selectById(parameterConfigId);
            if (sp != null) {
                inspectionSchemeParameter = new InspectionSchemeParameter();
                inspectionSchemeParameter.setId(sp.getId());
                inspectionSchemeParameter.setSchemeId(sp.getSchemeId());
                inspectionSchemeParameter.setVersionId(sp.getVersionId());
                inspectionSchemeParameter.setInspectItemId(sp.getInspectItemId());
                inspectionSchemeParameter.setItemConfigId(sp.getItemConfigId());
                inspectionSchemeParameter.setParameterId(sp.getParameterId());
                inspectionSchemeParameter.setRecordId(sp.getRecordId());
                inspectionSchemeParameter.setRecordVersionId(sp.getRecordVersionId());
                inspectionSchemeParameter.setRecordItemId(sp.getRecordItemId());
            } else {
                inspectionSchemeParameter = null;
            }
        } else {
            inspectionSchemeParameter = inspectionSchemeParameterMapper.selectById(parameterConfigId);
        }
        try {
            saveAndCalculateResults(results, inspectionSchemeParameter, inspectionOrder, filterNull);
            this.handlePlanModifyCount(inspectionOrder.getId(),inspectionSchemeParameter);
        } catch (DuplicateKeyException e) {
            log.info("生产计划[{}]记录数据重复:{}", inspectionOrder.getId(), e.getMessage());
            throw new BmosException(LimsResponseCode.EXECUTE_DATA_EXIST);
        } catch (DataIntegrityViolationException e) {
            handleDataTooLong(e);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public String pictureList(String value) {
        String emptyData = platformParameterClientImpl.getValueByCode(RECORD_EMPTY_DATA);
        if (StrUtil.isBlank(value) || StrUtil.equals(value,emptyData)){
            return null;
        }
        return executeAttachmentService.queryByIds(StrUtil.split(value, StrUtil.C_COMMA));
    }


    //    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void saveBusinessComponentsData(BusinessComponentBatchSaveDTO dto) {
//        List<ExecuteFormData> results = new ArrayList<>();
//        Long inspectOrderId = dto.getInspectOrderId();
//        ElnEntryContext context = new ElnEntryContext();
//        InspectionOrder inspectionOrder = inspectionOrderMapper.selectById(inspectOrderId);
//        Material material = materialMapper.selectById(inspectionOrder.getMaterialId());
//        InspectionSchemeVersionFullConfigDTO schemeVersionFullConfigDTO = inspectionSchemeVersionService.getInspectionSchemeVersionFullConfig(inspectionOrder.getSchemeVersionId());
//        Long inspectionParameterConfigId = dto.getInspectionParameterConfigId();
//        InspectionSchemeParameter inspectionSchemeParameter = inspectionSchemeParameterMapper.selectById(inspectionParameterConfigId);
//        context.setOrder(inspectionOrder);
//        context.setDto(dto);
//        context.setMaterial(material);
//        context.setSchemeVersionFullConfigDTO(schemeVersionFullConfigDTO);
//        context.setUnitCache(unitCache);
//        List<ComponentListVO> tree =
//                batchRecordComponentService.selectAutoFillComponentTree(inspectionSchemeParameter.getRecordVersionId(),
//                        inspectionSchemeParameter.getRecordItemId());
//        tree.forEach(component -> {
//            strategyMap.get(component.getComponentType()).handleBusinessComponent(results, component, context);
//        });
//        results.forEach(e -> {
//            e.setOperationType(ExecuteFormDataType.SAVE.getValue());
//            e.setOperationUser(SysUserHolder.getUser().getUserId());
//            e.setOperationTime(LocalDateTime.now());
//            e.setSystemCreate(true);
//        });
//        if (CollUtil.isNotEmpty(results)) {
//
//            List<ExecuteFormData> resultData = calculateData(results,
//                    ExecuteFormDataConverter.INSTANCE.convertQuery(dto));
//            if (CollUtil.isNotEmpty(resultData)) {
//                executeFormDataMapper.insertBatch(resultData);
//            }
//            executeFormDataMapper.insertBatch(results);
//        }
//    }

}
