package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.process.constant.ProcessConstant;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 工序步骤记录项配置实体
 */
@Getter
@Setter
@ToString
@TableName("bm_procedure_step_config")
public class ProcedureStepConfig extends BaseDO {

    /**
     * 工序步骤id
     * 实际为procedure_step_model的Id
     */
    private Long procedureStepId;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;

    /**
     * 流程节点Id
     */
    private String nodeId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺版本号
     */
    private String version;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 配置信息JSON
     */
    private String configInfo;

    /**
     * 组件id
     */
    private Long componentId;

    /**
     * field_id
     */
    private Long fieldId;

    @TableField(exist = false)
    private Boolean reuse;

    public Boolean getReuse() {
        return Objects.equals(procedureStepModelId, ProcessConstant.REUSE_PROCEDURE_STEP_ID);
    }

}
