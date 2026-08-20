package com.bmos.mes.service.dataset.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.record.BasicComponentTypeEnum;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.service.dataset.common.DatasetTrans;
import com.bmos.mes.service.dataset.common.enums.DatasetTransValueDataType;
import com.bmos.mes.service.dataset.handle.data.DataSetPointHandleData;
import com.bmos.mes.service.dataset.handle.data.DataSetProcess;
import com.bmos.mes.service.dataset.handle.data.ExecuteFormLoadingData;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.dataset.service.IDatasetService;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.FormDataProcedureInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FormLoadingBuilder {

    @Autowired
    private ExecuteFormDataService formDataService;

    @Autowired
    IDatasetService datasetService;

    private List<DatasetPoint> datasetPoints;


    public List<ExecuteFormLoadingData> build(List<Long> sortPlanIdList, DataSetProcess dataSetProcess) {
        List<ExecuteFormData> executeFormDataList = formDataService.selectByPlanIdList(sortPlanIdList);
        // 加载数据集
        List<Long> stepIdList = new ArrayList<>();
        List<Long> filedIdList = executeFormDataList.stream().map(executeFormData -> {
            stepIdList.add(executeFormData.getProcedureStepId());
            return executeFormData.getFieldId();
        }).collect(Collectors.toList());

        datasetPoints = datasetService.listByFieldIdsAndProcedureStepIds(filedIdList, stepIdList);
        // 数据集与表单数据关联
        List<ExecuteFormLoadingData> executeFormLoadingData = relationExeFormDataSet(executeFormDataList, datasetPoints, dataSetProcess);
        // 只保留最新数据
        return clearRevData(executeFormLoadingData);
    }


    private List<ExecuteFormLoadingData> relationExeFormDataSet(List<ExecuteFormData> formDataList, List<DatasetPoint> datasetPoints, DataSetProcess dataSetProcess) {
        Map<String, List<DatasetPoint>> datasetPointMap = datasetPoints.stream().collect(Collectors.groupingBy(datasetPoint -> String.format("%s_%s", datasetPoint.getFieldId(), datasetPoint.getProcedureStepId())));
        // 每个数据对应哪些数据点
        List<ExecuteFormLoadingData> loadingDataList = new ArrayList<>();
        Map<Long, FormDataProcedureInfo> infoMap = formDataService.selectProcessAndProcedureByFormDataIds(CollectionUtils.convertList(formDataList, ExecuteFormData::getId))
                .stream()
                .collect(Collectors.toMap(FormDataProcedureInfo::getFormDataId, Function.identity(), (v1, v2) -> v1));
        for (ExecuteFormData formData : formDataList) {
            String key = String.format("%s_%s", formData.getFieldId(), formData.getProcedureStepId());
            List<DatasetPoint> datasetPointList = datasetPointMap.get(key);
            if (CollectionUtil.isEmpty(datasetPointList)){
                continue;
            }
            ExecuteFormLoadingData loadingData = new ExecuteFormLoadingData()
                    .setPlanId(formData.getProductPlanId())
                    .setBatchNo(formData.getBatchNo())
                    .setProcessId(formData.getProcessId())
                    .setProcessVersion(formData.getProcessVersion())
                    .setRecordItemId(formData.getRecordItemId())
                    .setOperationTime(formData.getOperationTime())
                    .setComponentType(formData.getComponentType())
                    .setFieldId(formData.getFieldId())
                    .setReuse(formData.getReuse())
                    .setProcedureStepId(formData.getProcedureStepId())
                    .setFormDataExtInfo(JSON.parseObject(formData.getExtInfo(), ExecuteFormDataBaseExtInfo.class))
                    .setEmpty(BooleanUtil.isTrue(formData.getEmptyValue()))
                    .setRev(formData.getRev());
            loadingData.setProcessName(Optional.ofNullable(infoMap.get(formData.getId())).map(FormDataProcedureInfo::getProcessName).orElse(null));
            loadingData.setProcedureName(Optional.ofNullable(infoMap.get(formData.getId())).map(FormDataProcedureInfo::getProcedureName).orElse(null));
            List<DatasetPoint> collect = datasetPointList.stream()
                    .filter(item -> Objects.equals(dataSetProcess.get(item.getDatasetKey()), loadingData.getProcessId()))
                    .collect(Collectors.toList());
            loadingData.setProcedureChangeNumber(formData.getProcedureChangeNumber())
                    .setReuse(formData.getReuse())
                    .setProcessChangeNumber(formData.getProcessChangeNumber())
                    .setCopyVersion(formData.getCopyVersion());
            if (formData.getReuse()){
                loadingData.setProcessChangeNumber(DatasetTrans.DatasetTransExpression.DEFAULT_INTEGER_INDEX);
                loadingData.setProcedureChangeNumber(DatasetTrans.DatasetTransExpression.DEFAULT_INTEGER_INDEX);
            }
            loadingData.setType(judgeRenderValueType(formData.getComponentType()));
            loadingData.setValue(formData.getValue());
            loadingData.setDataSetPointHandleDataList(convert2DataSetPointHandleData(collect));
            loadingDataList.add(loadingData);
        }
        return loadingDataList;
    }

    private DatasetTransValueDataType judgeRenderValueType(String componentType) {
        if (StrUtil.equals(componentType, BasicComponentTypeEnum.CHECKBOX.getValue())
                || StrUtil.equals(componentType, BasicComponentTypeEnum.RADIO.getValue())){
            return DatasetTransValueDataType.CHECKBOX;
        }
        if (StrUtil.equals(componentType, BasicComponentTypeEnum.PHOTO.getValue())
                || StrUtil.equals(componentType, BusinessComponentTypeEnum.HANDLE_SUBMIT_SIGN.getValue())
                || StrUtil.equals(componentType, BusinessComponentTypeEnum.HANDLE_REVIEW_SIGN.getValue())){
            return DatasetTransValueDataType.IMAGE;
        }
        if (StrUtil.equals(componentType, BusinessComponentTypeEnum.EQUIPMENT_DATA_DRAW.getValue())) {
            return DatasetTransValueDataType.IMAGE_CAPTION;
        }
        return DatasetTransValueDataType.TEXT;
    }

    private List<DataSetPointHandleData> convert2DataSetPointHandleData(List<DatasetPoint> datasetPoints) {
        if (CollUtil.isEmpty(datasetPoints)){
            return new ArrayList<>();
        }
        return datasetPoints.stream().map(datasetPoint -> new DataSetPointHandleData()
                .setDataPoint(datasetPoint.getDatasetPointKey())
                .setDataSet(datasetPoint.getDatasetKey())
                .setDatasetType(datasetPoint.getType())).collect(Collectors.toList());

    }

    public List<DatasetPoint> getDatasetPoints() {
        if (CollUtil.isEmpty(datasetPoints)){
            return new ArrayList<>();
        }
        return datasetPoints;
    }

    private List<ExecuteFormLoadingData> clearRevData(List<ExecuteFormLoadingData> executeFormLoadingData) {
        Map<String, Long> keyMaxRev = new HashMap<>();
        Map<String, ExecuteFormLoadingData> curMaxRevData = new HashMap<>();
        for (ExecuteFormLoadingData executeFormLoadingDatum : executeFormLoadingData) {
            String curKey = String.format("%s_%s_%s_%s_%s_%s_%s_%s", executeFormLoadingDatum.getPlanId(), executeFormLoadingDatum.getFieldId(), executeFormLoadingDatum.getProcessChangeNumber(),
                    executeFormLoadingDatum.getProcedureChangeNumber(), executeFormLoadingDatum.getCopyVersion(), executeFormLoadingDatum.getReuse(), executeFormLoadingDatum.getProcedureStepId(),
                    executeFormLoadingDatum.getProcessChangeNumber());
            if (!keyMaxRev.containsKey(curKey)) {
                keyMaxRev.put(curKey, executeFormLoadingDatum.getRev());
                curMaxRevData.put(curKey, executeFormLoadingDatum);
            } else if (keyMaxRev.get(curKey) < executeFormLoadingDatum.getRev()) {
                keyMaxRev.put(curKey, executeFormLoadingDatum.getRev());
                curMaxRevData.put(curKey, executeFormLoadingDatum);
            }
        }
        return new ArrayList<>(curMaxRevData.values());
    }

}
