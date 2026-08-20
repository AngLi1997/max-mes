package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName ProcedureTimeVO
 * @Description 工序执行时长
 * @Author Ren Jin Guang
 * @Date 2024/9/2 17:35
 */
@Setter
@Getter
@ToString
public class ProcedureTimeVO {

    @ApiModelProperty("计划id")
    private Long planItemId;

    @ApiModelProperty("工序配置详情")
    private String procedureConfig;

    @ApiModelProperty("工序计划时长相关信息")
    private String procedureList;
}
