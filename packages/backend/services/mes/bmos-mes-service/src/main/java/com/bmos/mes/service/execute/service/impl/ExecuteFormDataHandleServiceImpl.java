package com.bmos.mes.service.execute.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.enums.record.BusinessTimeComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.model.execute.ExecuteFormDataMultiTimeExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.RecordItemLatestDataQueryDTO;
import com.bmos.mes.service.execute.enums.ExecuteFormDataType;
import com.bmos.mes.service.execute.mapper.ExecuteFormDataMapper;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.service.ExecuteFormDataHandleService;
import com.bmos.mes.service.execute.service.ExecuteRecordCopyService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.preparation.measure.dto.FormDataFilterDTO;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExecuteFormDataHandleServiceImpl implements ExecuteFormDataHandleService {


    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ExecuteFormDataMapper executeFormDataMapper;

    @Resource
    private PlatformParameterClientImpl platformParameterClient;

    @Resource
    private ExecuteRecordCopyService executeRecordCopyService;

    @Override
    public List<ExecuteFormData> fillFormDataAndFilter(FormDataFilterDTO dto) {
        if (CollUtil.isEmpty(dto.getDataList())) {
            return new ArrayList<>();
        }
        ExecuteFormData first = CollUtil.getFirst(dto.getDataList());
        if (first == null || first.getProductPlanId() == null || dto.getProcedureStepModelId() == null) {
            return new ArrayList<>();
        }
        Plan plan = planMapper.selectById(first.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(dto.getProcedureStepModelId());
        if (procedureStepModel == null ){
            throw new BmosException(MesResponseCode.PROCEDURE_STEP_NOT_EXIST);
        }
        RecordItemLatestDataQueryDTO queryDTO = getRecordItemLatestDataQueryDTO(procedureStepModel, dto.getDataList());
        List<ExecuteFormData> inDB = executeFormDataMapper.selectByStep(queryDTO);
        fillFormData(dto, procedureStepModel, inDB);
        List<ExecuteFormData> latest = ExecuteFormDataConverter.INSTANCE.filterLatest(inDB);
        Map<Long, ExecuteFormData> dataMap = CollectionUtils.convertMap(latest, ExecuteFormData::getFieldId);
        // 过滤出组件值不存在或者组件值有更新的数据
        return dto.getDataList().stream().filter(e -> {
            return dataMap.get(e.getFieldId()) == null
                    || !StrUtil.equals(dataMap.get(e.getFieldId()).getValue(), e.getValue());
        }).collect(Collectors.toList());
    }


    /**
     * 填充ExecuteFormData
     * @param dto
     * @param procedureStepModel
     * @param inDB
     * @return
     */
    private void fillFormData(FormDataFilterDTO dto, ProcedureStepModel procedureStepModel, List<ExecuteFormData> inDB) {
        ExecuteFormData first = CollUtil.getFirst(dto.getDataList());
        // 查询换班班次
        ExecuteRecordCopy copy = executeRecordCopyService.getCurrentChangeRecord(first.getProductPlanId(), procedureStepModel.getId(), first.getCopyVersion());
        // 需要记录修订而非更新的业务组件
        Set<String> modifyType = Sets.newHashSet(BusinessComponentTypeEnum.HANDLE_SUBMIT_SIGN.getValue(), BusinessComponentTypeEnum.HANDLE_REVIEW_SIGN.getValue());
        List<Long> longs = CollectionUtils.convertList(dto.getDataList(), ExecuteFormData::getFieldId);
        Long rev = executeFormDataMapper.getNextRev(first.getProductPlanId(), longs);
        String valueByCode = platformParameterClient.getValueByCode(BusinessParameterCodeConstants.PLATFORM_SYS_TIME_FORMAT);
        JSONObject patternObj = JSONUtil.parseObj(valueByCode);
        Set<Long> existedFieldIds = CollectionUtils.convertSet(inDB, ExecuteFormData::getFieldId);
        dto.getDataList().forEach(e -> {
            if (modifyType.contains(e.getComponentType())){
                e.setOperationType(existedFieldIds.contains(e.getFieldId()) ?
                        ExecuteFormDataType.MODIFY.getValue() : ExecuteFormDataType.SAVE.getValue());
                e.setOperationUser(StrUtil.isNotEmpty(e.getOperationUser()) ? e.getOperationUser() : SysUserHolder.getUser().getUserId());
            } else {
                if (StrUtil.isEmpty(e.getOperationType())) {
                    e.setOperationType(existedFieldIds.contains(e.getFieldId()) ?
                            ExecuteFormDataType.UPDATE.getValue() : ExecuteFormDataType.SAVE.getValue());
                }
                e.setOperationUser(StrUtil.isNotEmpty(e.getOperationUser()) ? e.getOperationUser() : SysUserHolder.getUser().getUserId());
            }
            // 处理日期时间业务组件格式
            handleValuePattern(e, patternObj);
            e.setOperationTime(e.getOperationTime() == null ? LocalDateTime.now(): e.getOperationTime());
            e.setRev(rev);
            e.setSystemCreate(false);
            e.setProcessChangeNumber(copy == null ? 0 : copy.getProcessChangeNumber());
            e.setProcedureChangeNumber(copy == null ? 0 : copy.getProcedureChangeNumber());
        });
    }


    /**
     * 处理日期时间业务组件的格式
     * @param e
     * @param patternObj 平台参数配置中的日期格式
     */
    private static void handleValuePattern(ExecuteFormData e, JSONObject patternObj) {
        BusinessTimeComponentTypeEnum timeType = BusinessTimeComponentTypeEnum.getEnumByValue(e.getComponentType());
        if (timeType == null) {
            return;
        }
        String pattern = patternObj.get(timeType.getPatternProperty(), String.class);
        if (StrUtil.isEmpty(pattern)) {
            throw new BmosException(MesResponseCode.PARAM_TIME_FORMAT_NOT_EXIST);
        }
        if (StrUtil.isNotEmpty(e.getValue()) && StrUtil.isEmpty(e.getExtInfo()) && !timeType.isMultiLine()) {
            e.setExtInfo(JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(String.valueOf(TimeUtil.getTimestamp(LocalDateTimeUtil.parse(e.getValue(), timeType.getDefaultPattern()))))));
        }
        // 对多条换行的数据替换处理
        if (timeType.isMultiLine()) {
            List<String> split = StrUtil.split(e.getValue(), StrUtil.LF);
            List<Long> timeStampList = new ArrayList<>();
            List<String> collect = split.stream().map(s -> {
                boolean dateFormat = TimeUtil.isDateFormat(s, timeType.getDefaultPattern());
                if (!dateFormat) {
                    timeStampList.add(null);
                    return s;
                }
                Long timestamp = TimeUtil.getTimestamp(LocalDateTimeUtil.parse(s, timeType.getDefaultPattern()));
                timeStampList.add(timestamp);
                return LocalDateTimeUtil.format(LocalDateTimeUtil.of(timestamp), pattern);
            }).collect(Collectors.toList());
            e.setValue(StrUtil.join(StrUtil.LF, collect));
            e.setExtInfo(JsonUtils.toJsonString(new ExecuteFormDataMultiTimeExtInfo(timeStampList, true)));
            return;
        }
        ExecuteFormDataBaseExtInfo bean = JSONUtil.toBean(e.getExtInfo(), ExecuteFormDataBaseExtInfo.class);
        if (bean == null || StrUtil.isEmpty(bean.getTimeStamp())) {
            throw new BmosException(MesResponseCode.TIME_BUSINESS_COMPONENT_HAS_NO_TIMESTAMP, timeType.getName());
        }
        String format = LocalDateTimeUtil.format(LocalDateTimeUtil.of(Long.parseLong(bean.getTimeStamp())), pattern);
        e.setValue(format);
    }

    private RecordItemLatestDataQueryDTO getRecordItemLatestDataQueryDTO(ProcedureStepModel procedureStepModel,
                                                                         List<ExecuteFormData> dataList) {
        ExecuteFormData first = CollUtil.getFirst(dataList);
        RecordItemLatestDataQueryDTO dto = new RecordItemLatestDataQueryDTO();
        dto.setProductPlanId(first.getProductPlanId());
        dto.setProcedureStepId(procedureStepModel.getProcedureStepId());
        dto.setRecordItemId(procedureStepModel.getRecordItemId());
        dto.setReuse(procedureStepModel.getReusable());
        dto.setCopyVersion(first.getCopyVersion());
        dto.setFieldIdList(CollectionUtils.convertList(dataList, ExecuteFormData::getFieldId));
        return dto;
    }

    /**
     * 获取ExecuteFormData处理后的value
     * 对于时间日期类型的组件 value会根据平台参数配置进行格式化
     * @param e
     * @param patternObj
     * @return
     */
    private String getFormatFormDataValue(ExecuteFormData e, JSONObject patternObj) {
        BusinessTimeComponentTypeEnum timeType = BusinessTimeComponentTypeEnum.getEnumByValue(e.getComponentType());
        if (timeType == null) {
            return e.getValue();
        }
        String pattern = patternObj.get(timeType.getPatternProperty(), String.class);
        if (StrUtil.isEmpty(pattern)) {
            throw new BmosException(MesResponseCode.PARAM_TIME_FORMAT_NOT_EXIST);
        }
        if (StrUtil.isNotEmpty(e.getValue()) && StrUtil.isEmpty(e.getExtInfo()) && !timeType.isMultiLine()) {
            e.setExtInfo(JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(String.valueOf(TimeUtil.getTimestamp(LocalDateTimeUtil.parse(e.getValue(), timeType.getDefaultPattern()))))));
        }
        // 对多条换行的数据替换处理
        if (timeType.isMultiLine()) {
            ExecuteFormDataMultiTimeExtInfo extInfo = JsonUtils.parseObject(e.getExtInfo(), ExecuteFormDataMultiTimeExtInfo.class);
            if (extInfo == null || CollUtil.isEmpty(extInfo.getTimestampList())) {
                return e.getValue();
            }
            List<String> collect = extInfo.getTimestampList().stream().map(s -> {
                return LocalDateTimeUtil.format(LocalDateTimeUtil.of(s), pattern);
            }).collect(Collectors.toList());
            return StrUtil.join(StrUtil.LF, collect);
        }
        ExecuteFormDataBaseExtInfo bean = JSONUtil.toBean(e.getExtInfo(), ExecuteFormDataBaseExtInfo.class);
        if (bean == null || StrUtil.isEmpty(bean.getTimeStamp())) {
            throw new BmosException(MesResponseCode.TIME_BUSINESS_COMPONENT_HAS_NO_TIMESTAMP, timeType.getName());
        }
        return LocalDateTimeUtil.format(LocalDateTimeUtil.of(Long.parseLong(bean.getTimeStamp())), pattern);

    }



}
