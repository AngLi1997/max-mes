package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * 工序步骤-角色关联实体
 */
@Getter
@Setter
@ToString
@Builder
@TableName("bm_procedure_step_role")
public class ProcedureStepRole {
    @Tolerate
    public ProcedureStepRole(){

    }
    /**
     * 工序步骤id
     * 注意:此处为工步模型id
     */
    private Long procedureStepId;

    /**
     * 角色id
     */
    private Long roleId;
}
