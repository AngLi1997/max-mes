package com.bmos.mes.service.dataset.handle.data;

import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ExecuteFormLoadingData extends BaseLoadingData{

    /**
     * 生产计划id
     */
    private Long planId;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 组件id
     */
    private Long fieldId;

    /**
     * 组件类型
     */
    private String componentType;

    /**
     * 历史工序步骤id
     */
    private Long procedureStepId;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 当前值的版本
     */
    private Long rev;

    /**
     * formData的扩展信息
     */
    private ExecuteFormDataBaseExtInfo formDataExtInfo;

    private LocalDateTime operationTime;

    /**
     * 是否是录入的空值
     */
    private boolean isEmpty;


}
