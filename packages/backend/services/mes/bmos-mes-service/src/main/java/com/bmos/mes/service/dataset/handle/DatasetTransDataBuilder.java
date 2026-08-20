package com.bmos.mes.service.dataset.handle;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.dataset.common.DatasetTransData;
import com.bmos.mes.service.dataset.common.DatasetTransDsData;
import com.bmos.mes.service.dataset.handle.data.DataSetPointHandleData;
import com.bmos.mes.service.dataset.handle.data.ExecuteFormLoadingData;

import java.util.List;
import java.util.Map;

public class DatasetTransDataBuilder {
    public static DatasetTransData build(List<ExecuteFormLoadingData> executeFormLoadingData) {
        DatasetTransData datasetTransData = new DatasetTransData();
        if (CollUtil.isEmpty(executeFormLoadingData)) {
            return datasetTransData;
        }
        for (ExecuteFormLoadingData loadingData : executeFormLoadingData) {
            List<DataSetPointHandleData> dataPointHandleDataList = loadingData.getDataSetPointHandleDataList();
            if (CollUtil.isEmpty(dataPointHandleDataList)) {
                // 没有配置数据集 则直接进行忽略
                continue;
            }
            datasetTransData.putData(dataPointHandleDataList, loadingData);
        }
        return datasetTransData;
    }
}
