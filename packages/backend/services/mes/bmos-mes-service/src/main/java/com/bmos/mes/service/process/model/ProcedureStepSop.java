package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName ProcedureStepSop
 * @Description 工艺配置绑定操作规程表
 * @Author Ren Jin Guang
 * @Date 2024/12/13 16:49
 */
@Getter
@Setter
@ToString
@TableName("bm_procedure_step_sop")
public class ProcedureStepSop extends BaseDO {

    @ApiModelProperty("工步主键id")
    private Long stepModelId;

    @ApiModelProperty("操作规程id")
    private Long operationSopId;
}
