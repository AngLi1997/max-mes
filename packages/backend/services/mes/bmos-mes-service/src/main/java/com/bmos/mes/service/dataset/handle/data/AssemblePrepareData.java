package com.bmos.mes.service.dataset.handle.data;

import com.bmos.mes.service.plan.info.model.Plan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 组装数据所需要的前置数据
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class AssemblePrepareData extends PlanBatchDocumentData {

    /**
     * 最新的作业数据
     */
    private List<ExecuteFormLoadingData> executeFormLoadingData;

    /**
     * 所有批次下的所有复制版本
     */
    private PlanCopyVersion planCopyVersion;

    /**
     * 所有批次下的附件数据
     */
    private PlanAttachment planAttachment;

    /**
     * 数据集与工艺之间的绑定关系
     */
    private DataSetProcess dataSetProcess;

    /**
     * 需要的所有的生产计划数据
     */
    private List<Plan> planList;

}
