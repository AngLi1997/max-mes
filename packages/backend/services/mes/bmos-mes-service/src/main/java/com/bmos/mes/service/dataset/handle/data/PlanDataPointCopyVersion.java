package com.bmos.mes.service.dataset.handle.data;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.multi.RowKeyTable;
import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;

import java.util.List;

/**
 * 批次下 各个数据点对应的复制记录 row->数据集索引 coll-> 数据点索引 value为当前换班下的所有复制记录
 */
public class PlanDataPointCopyVersion extends RowKeyTable<String, String, PlanChangeTeamCopyVersion> {

    public void putAllAData(RowKeyTable<Long, Long, List<DatasetPoint>> datasetPointTable, RowKeyTable<Long, Long, List<ExecuteRecordCopy>> recordCopyTable) {

        for (Long stepId : datasetPointTable.rowKeySet()) {
            for (Long itemId : datasetPointTable.columnKeySet()){
                List<DatasetPoint> curDatasetPointList = datasetPointTable.get(stepId, itemId);
                List<ExecuteRecordCopy> curRecordCopyList = recordCopyTable.get(stepId, itemId);
                if (CollUtil.isEmpty(curRecordCopyList)){
                    continue;
                }
                if (CollUtil.isEmpty(curDatasetPointList)){
                    continue;
                }
                PlanChangeTeamCopyVersion planChangeTeamCopyVersion = new PlanChangeTeamCopyVersion();
                planChangeTeamCopyVersion.putAllData(curRecordCopyList);
                // 进行排序
                planChangeTeamCopyVersion.sort();
                for (DatasetPoint datasetPoint : curDatasetPointList) {
                    this.put(datasetPoint.getDatasetKey(), datasetPoint.getDatasetPointKey(), planChangeTeamCopyVersion);
                }
            }
        }
    }

}
