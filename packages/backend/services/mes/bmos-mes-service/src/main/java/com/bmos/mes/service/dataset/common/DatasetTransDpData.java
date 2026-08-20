package com.bmos.mes.service.dataset.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.multi.RowKeyTable;
import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mes.service.dataset.handle.data.*;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据点
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 15:20
 */
@Data
public class DatasetTransDpData {

    /**
     * 数据点类型 POINT 批记录数据(数据点) LOT_RELEASE_LINK 批签发引用 DYNAMIC_REPORT 动态数据填报
     * {@link DatasetType}
     */
    private DatasetType type;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 数据点值
     * 层级 工艺班次下标 工序班次下标 复制版本下标 已被废弃
     */
    @Deprecated
    private List<List<List<DatasetTransValueData>>> values;

    /**
     * 数据点值
     * row -> 工艺换班 col-> 工序换班 value-> 当前工艺换班以及工序换班下的不同复制版本的作业值
     */
    private RowKeyTable<Integer, Integer, List<DatasetTransValueData>> newValues = new RowKeyTable<>();


    public void putData(DataSetPointHandleData dataSetPointHandleData, ExecuteFormLoadingData loadingData) {
        this.type = dataSetPointHandleData.getDatasetType();
        if (this.getReuse() == null){
            this.reuse = loadingData.getReuse();
        }
        DatasetTransValueData datasetTransValueData = convert2DatasetTransValueData(loadingData);
        datasetTransValueData.setEmpty(loadingData.isEmpty());
        List<DatasetTransValueData> transValueDataList = this.newValues.get(loadingData.getProcessChangeNumber(), loadingData.getProcedureChangeNumber());
        if (CollUtil.isEmpty(transValueDataList)){
            transValueDataList = Lists.newArrayList(datasetTransValueData);
            this.newValues.put(loadingData.getProcessChangeNumber(), loadingData.getProcedureChangeNumber(), transValueDataList);
        } else {
            transValueDataList.add(datasetTransValueData);
        }
    }

    public void putData(DataSetPointHandleData dataSetPointHandleData, DynamicRenderingData dynamicRenderingDatum) {
        this.type = dataSetPointHandleData.getDatasetType();
        DatasetTransValueData datasetTransValueData = convert2DatasetTransValueData(dynamicRenderingDatum);
        List<DatasetTransValueData> transValueDataList = this.newValues.get(0, 0);
        if (CollUtil.isEmpty(transValueDataList)){
            transValueDataList = Lists.newArrayList(datasetTransValueData);
            this.newValues.put(0, 0, transValueDataList);
        } else {
            transValueDataList.add(datasetTransValueData);
        }
    }

    public void putData(DataSetPointHandleData dataSetPointHandleData, PlanLoadingData planLoadingData) {
        this.type = dataSetPointHandleData.getDatasetType();
        DatasetTransValueData datasetTransValueData = convert2DatasetTransValueData(planLoadingData);
        List<DatasetTransValueData> transValueDataList = this.newValues.get(0, 0);
        if (CollUtil.isEmpty(transValueDataList)){
            transValueDataList = Lists.newArrayList(datasetTransValueData);
            this.newValues.put(0, 0, transValueDataList);
        } else {
            transValueDataList.add(datasetTransValueData);
        }
    }

    private DatasetTransValueData convert2DatasetTransValueData(BaseLoadingData curLoadingData) {
        DatasetTransValueData datasetTransValueData = new DatasetTransValueData();
        datasetTransValueData.setType(curLoadingData.getType());
        datasetTransValueData.setValue(curLoadingData.getValue());
        datasetTransValueData.setCopyVersion(curLoadingData.getCopyVersion());
        datasetTransValueData.setProcedureChangeNumber(curLoadingData.getProcedureChangeNumber());
        datasetTransValueData.setProcessChangeNumber(curLoadingData.getProcessChangeNumber());
        datasetTransValueData.setProcessName(curLoadingData.getProcessName());
        datasetTransValueData.setProcedureName(curLoadingData.getProcedureName());
        return datasetTransValueData;
    }

    /**
     * 对newValues内的所有进行排序
     * @param copyVersion
     */
    public void sort(PlanChangeTeamCopyVersion copyVersion) {
        if (copyVersion== null || copyVersion.isEmpty()){
            return ;
        }
        for (Integer processChange : this.newValues.rowKeySet()) {
            for (Integer procedureChange : this.newValues.columnKeySet()){
                List<DatasetTransValueData> datasetTransValueDataList = this.newValues.get(processChange, procedureChange);
                if (CollUtil.isEmpty(datasetTransValueDataList)){
                    continue;
                }
                List<Long> copyVersionList = copyVersion.get(processChange, procedureChange);
                if (CollUtil.isEmpty(copyVersionList)){
                    continue;
                }
                Map<Long, DatasetTransValueData> datasetTransValueDataMap = datasetTransValueDataList.stream().collect(Collectors.toMap(DatasetTransValueData::getCopyVersion, Function.identity()));
                List<DatasetTransValueData> sortCopyDataList = new ArrayList<>();
                for (Long curCopyVersion : copyVersionList) {
                    sortCopyDataList.add(datasetTransValueDataMap.get(curCopyVersion));
                }
                this.newValues.put(processChange, procedureChange, sortCopyDataList);
             }
        }
    }
}
