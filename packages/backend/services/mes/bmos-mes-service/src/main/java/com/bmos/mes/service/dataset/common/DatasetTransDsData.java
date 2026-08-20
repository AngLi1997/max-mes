package com.bmos.mes.service.dataset.common;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.dataset.handle.data.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据集
 * key：数据点流水号
 * value 数据点
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 15:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DatasetTransDsData extends HashMap<String, DatasetTransDpData> {

    public void putData(DataSetPointHandleData dataSetPointHandleData, ExecuteFormLoadingData loadingData) {
        DatasetTransDpData datasetTransDpData = this.getOrDefault(dataSetPointHandleData.getDataPoint(), new DatasetTransDpData());
        datasetTransDpData.putData(dataSetPointHandleData, loadingData);
        this.put(dataSetPointHandleData.getDataPoint(), datasetTransDpData);
    }

    public void putData(DataSetPointHandleData dataSetPointHandleData, DynamicRenderingData dynamicRenderingDatum) {
        DatasetTransDpData datasetTransDpData = new DatasetTransDpData();
        datasetTransDpData.putData(dataSetPointHandleData, dynamicRenderingDatum);
        this.put(dataSetPointHandleData.getDataPoint(), datasetTransDpData);
    }

    public void putData(DataSetPointHandleData dataSetPointHandleData, PlanLoadingData planLoadingData) {
        DatasetTransDpData datasetTransDpData = new DatasetTransDpData();
        datasetTransDpData.putData(dataSetPointHandleData, planLoadingData);
        this.put(dataSetPointHandleData.getDataPoint(), datasetTransDpData);
    }

    public void sort(Map<String, PlanChangeTeamCopyVersion> row) {
        for (String dataPoint : this.keySet()) {
            if (CollUtil.isEmpty(row)){
                continue;
            }
            this.get(dataPoint).sort(row.get(dataPoint));
        }
    }
}
