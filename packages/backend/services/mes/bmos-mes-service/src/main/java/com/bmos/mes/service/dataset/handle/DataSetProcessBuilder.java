package com.bmos.mes.service.dataset.handle;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.dataset.handle.data.DataSetPointHandleData;
import com.bmos.mes.service.dataset.handle.data.DataSetProcess;
import com.bmos.mes.service.dataset.handle.data.DynamicRenderingData;
import com.bmos.mes.service.dataset.model.Dataset;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.dataset.service.IDatasetService;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataSetProcessBuilder {

    @Autowired
    IDatasetService datasetService;

    @Autowired
    PlanService planService;


    public DataSetProcess build(List<DatasetPoint> datasetPoints, List<DynamicRenderingData> dynamicRenderingData, Long planId) {
        DataSetProcess map = new DataSetProcess();
        if (CollUtil.isNotEmpty(datasetPoints)){
            Set<String> dataSet = datasetPoints.stream().map(DatasetPoint::getDatasetKey).collect(Collectors.toSet());
            List<Dataset> datasetList = datasetService.queryByDataSetKeyList(dataSet);
            map.putAll(datasetList.stream().collect(Collectors.toMap(Dataset::getDatasetKey, Dataset::getProcessId)));
        }
        if (CollUtil.isEmpty(dynamicRenderingData)){
            return map;
        }
        Plan plan = planService.getById(planId);
        for (DynamicRenderingData dynamicRenderingDatum : dynamicRenderingData) {
            for (DataSetPointHandleData dataSetPointHandleData : dynamicRenderingDatum.getDataSetPointHandleDataList()) {
                map.put(dataSetPointHandleData.getDataSet(), plan.getProcessId());
            }
        }
        return map;
    }
}
