package com.bmos.mes.service.dataset.handle.data;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.multi.RowKeyTable;
import com.bmos.mes.service.execute.model.ExecuteRecordCopy;
import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 当前数据点下的当前班次的所有复制版本 row -> 工艺换班 col->工序换班 value -> 复制版本
 */
public class PlanChangeTeamCopyVersion extends RowKeyTable<Integer, Integer, List<Long>> {

    public void putAllData(List<ExecuteRecordCopy> curRecordCopyList) {
        for (ExecuteRecordCopy recordCopy : curRecordCopyList) {
            if (CollUtil.isNotEmpty(this.get(recordCopy.getProcessChangeNumber(), recordCopy.getProcedureChangeNumber()))){
                this.get(recordCopy.getProcessChangeNumber(), recordCopy.getProcedureChangeNumber()).add(recordCopy.getVersion());
            } else {
                this.put(recordCopy.getProcessChangeNumber(), recordCopy.getProcedureChangeNumber(), Lists.newArrayList(recordCopy.getVersion()));
            }
        }
    }

    public void sort() {
        for (Integer processChangeNum : this.rowKeySet()) {
            for (Integer prcedureChangeNum : this.columnKeySet()) {
                List<Long> copyVersionList = this.get(processChangeNum, prcedureChangeNum);
                if (CollUtil.isEmpty(copyVersionList)){
                    continue;
                }
                List<Long> sortCopyVersionList = copyVersionList.stream().sorted(Comparator.comparing(Long::intValue)).collect(Collectors.toList());
                this.put(processChangeNum, prcedureChangeNum, sortCopyVersionList);
            }
        }
    }
}
