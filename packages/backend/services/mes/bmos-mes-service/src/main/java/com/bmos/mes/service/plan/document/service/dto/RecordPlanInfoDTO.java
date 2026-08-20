package com.bmos.mes.service.plan.document.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 生产批号的工艺绑定了哪些模板
 */
@Getter
@Setter
@ApiModel("[版本管理]生产信息DTO")
public class RecordPlanInfoDTO{

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id", required = true)
    @NotNull
    private Long planId;

    /**
     * 批记录模板信息id
     */
    @ApiModelProperty(value = "批记录模板信息id", required = true)
    @NotNull
    private Long templateInfoId;

}
