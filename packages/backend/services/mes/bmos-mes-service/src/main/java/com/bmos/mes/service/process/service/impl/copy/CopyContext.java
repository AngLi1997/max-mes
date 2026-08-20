package com.bmos.mes.service.process.service.impl.copy;

import com.bmos.mes.service.process.dto.RelationBatchRecordItemDTO;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.record.model.BatchRecordVersion;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
public class CopyContext {

    /**
     * 当复制工艺时为true，升版时为false
     */
    private boolean isCopy;

    /**
     * 工序排序
     */
    private AtomicInteger sort;

    /**
     * 归档顺序map
     */
    private Map<String, ProcessRecordOrder> orderMap;

    /**
     *
     */
    private Map<Long, BatchRecordVersion> versionMap;

    /**
     *
     */
    private Map<Long, RelationBatchRecordItemDTO> itemMap;

    /**
     * 工步模型列表
     */
    private Map<Long, List<ProcedureStepModel>> stepModelMap;

    /**
     * 工步组件配置
     */
    private List<ProcedureStepConfig> stepConfigList;

    /**
     * 工步操规map
     */
    private Map<Long, List<ProcedureStepSop>> sopMap;

    /**
     * 工步班组map
     */
    private Map<Long, List<ProcedureStepRole>> stepGroupMap;

    /**
     * 工步条件表达式map
     */
    private Map<Long, List<ProcedureExpression>> expressionMap;

}
