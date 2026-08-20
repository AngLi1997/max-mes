package com.bmos.mes.service.dataset.handle.data;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.multi.RowKeyTable;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 所有批次所有班次复制的版本 key-> 批次id
 */
public class PlanCopyVersion extends HashMap<Long, PlanDataPointCopyVersion> {


    public void putAllData(List<ExecuteRecordCopy> recordCopyList, List<DatasetPoint> datasetPoints, List<BatchRecordComponent> components ) {
        Map<Long, List<ExecuteRecordCopy>> planExecuteRecordCopyMap = recordCopyList.stream().collect(Collectors.groupingBy(ExecuteRecordCopy::getProductPlanId));
        // 以为数据点表中recordItemid为空，需要获取recordItemd
        Map<Long, Long> itemIdMap = CollectionUtils.convertMap(components, BatchRecordComponent::getFieldId, BatchRecordComponent::getRecordItemId);
        RowKeyTable<Long, Long, List<DatasetPoint>> datasetPointTable = buildDatasetPointTable(datasetPoints, itemIdMap);
        for (Long planId : planExecuteRecordCopyMap.keySet()) {
            RowKeyTable<Long, Long, List<ExecuteRecordCopy>> recordCopyTable = buildRecordCopyTable(planExecuteRecordCopyMap.get(planId));
            PlanDataPointCopyVersion copyVersionMap = new PlanDataPointCopyVersion();
            copyVersionMap.putAllAData(datasetPointTable, recordCopyTable);
            this.put(planId, copyVersionMap);
        }
    }

    private RowKeyTable<Long, Long, List<ExecuteRecordCopy>> buildRecordCopyTable(List<ExecuteRecordCopy> executeRecordCopies) {
        RowKeyTable<Long, Long, List<ExecuteRecordCopy>> recordCopyTable = new RowKeyTable<>();
        for (ExecuteRecordCopy executeRecordCopy : executeRecordCopies) {
            if (CollUtil.isNotEmpty(recordCopyTable.get(executeRecordCopy.getProcedureStepId(), executeRecordCopy.getRecordItemId()))){
                recordCopyTable.get(executeRecordCopy.getProcedureStepId(), executeRecordCopy.getRecordItemId()).add(executeRecordCopy);
            } else {
                recordCopyTable.put(executeRecordCopy.getProcedureStepId(), executeRecordCopy.getRecordItemId(), Lists.newArrayList(executeRecordCopy));
            }
        }
        return recordCopyTable;
    }

    private RowKeyTable<Long, Long, List<DatasetPoint>> buildDatasetPointTable(List<DatasetPoint> datasetPoints, Map<Long, Long> itemIdMap) {
        RowKeyTable<Long, Long, List<DatasetPoint>> datasetPointTable = new RowKeyTable<>();
        for (DatasetPoint datasetPoint : datasetPoints) {
            if (CollUtil.isNotEmpty(datasetPointTable.get(datasetPoint.getProcedureStepId(), itemIdMap.get(datasetPoint.getFieldId())))){
                datasetPointTable.get(datasetPoint.getProcedureStepId(), itemIdMap.get(datasetPoint.getFieldId())).add(datasetPoint);
            } else {
                datasetPointTable.put(datasetPoint.getProcedureStepId(), itemIdMap.get(datasetPoint.getFieldId()), Lists.newArrayList(datasetPoint));
            }
        }
        return datasetPointTable;
    }

}
