package com.bmos.mes.service.dataset.handle;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.dataset.handle.data.PlanCopyVersion;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.execute.service.ExecuteRecordCopyService;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanCopyVersionBuilder {

    @Autowired
    ExecuteRecordCopyService executeRecordCopyService;

    @Autowired
    BatchRecordComponentService batchRecordComponentService;

    public PlanCopyVersion build(List<DatasetPoint> datasetPoints, List<Long> sortPlanIdList) {
        // 加载批次中所有复制版本
        List<ExecuteRecordCopy> recordCopyList = executeRecordCopyService.getByPlanIdList(sortPlanIdList);
        PlanCopyVersion planCopyVersion = new PlanCopyVersion();
        if (CollUtil.isEmpty(recordCopyList)){
            return planCopyVersion;
        }
        if (CollUtil.isEmpty(datasetPoints)){
            return planCopyVersion;
        }
        List<Long> fieldIdList = datasetPoints.stream().map(DatasetPoint::getFieldId).collect(Collectors.toList());
        List<BatchRecordComponent> components = batchRecordComponentService.getByFieldIdList(fieldIdList);
        planCopyVersion.putAllData(recordCopyList, datasetPoints, components);
        return planCopyVersion;
    }

}
