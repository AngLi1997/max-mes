package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("组件值列表查询")
public class FormDataListQueryDTO {

    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "历史工序步骤id",required = true)
    @NotNull
    private Long procedureStepId;

    @ApiModelProperty(value = "组件id",required = true)
    @NotNull
    private Long fieldId;

    @ApiModelProperty("是否查询作废数据")
    private Boolean discard;

    @ApiModelProperty("复制版本号")
    @NotNull
    private Long copyVersion;

    @NotNull
    @ApiModelProperty("是否复用")
    private Boolean reuse;
}
