package com.bmos.mes.service.dataset.common;

import com.bmos.mes.service.dataset.handle.data.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 数据集数据组装
 * key：数据集流水号
 * value：数据集
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 15:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DatasetTransData extends HashMap<String, DatasetTransDsData> {

    public void putData(List<DataSetPointHandleData> dataPointHandleDataList, ExecuteFormLoadingData loadingData) {
        for (DataSetPointHandleData dataSetPointHandleData : dataPointHandleDataList) {
            DatasetTransDsData datasetTransDsData = this.getOrDefault(dataSetPointHandleData.getDataSet(), new DatasetTransDsData());
            datasetTransDsData.putData(dataSetPointHandleData, loadingData);
            this.put(dataSetPointHandleData.getDataSet(), datasetTransDsData);
        }
    }

    public void putData(List<DataSetPointHandleData> dataPointHandleDataList, DynamicRenderingData dynamicRenderingDatum) {
        for (DataSetPointHandleData dataSetPointHandleData : dataPointHandleDataList) {
            DatasetTransDsData datasetTransDsData = this.getOrDefault(dataSetPointHandleData.getDataSet(), new DatasetTransDsData());
            datasetTransDsData.putData(dataSetPointHandleData, dynamicRenderingDatum);
            this.put(dataSetPointHandleData.getDataSet(), datasetTransDsData);
        }
    }

    public void putData(List<DataSetPointHandleData> dataPointHandleDataList, PlanLoadingData planLoadingData) {
        for (DataSetPointHandleData dataSetPointHandleData : dataPointHandleDataList) {
            DatasetTransDsData datasetTransDsData = this.getOrDefault(dataSetPointHandleData.getDataSet(), new DatasetTransDsData());
            datasetTransDsData.putData(dataSetPointHandleData, planLoadingData);
            this.put(dataSetPointHandleData.getDataSet(), datasetTransDsData);
        }
    }

    public void sort(PlanDataPointCopyVersion copyVersion) {
        for (String dataSet : this.keySet()) {
            if (copyVersion == null || copyVersion.isEmpty()){
                continue;
            }
            this.get(dataSet).sort(copyVersion.getRow(dataSet));
        }
    }
}
