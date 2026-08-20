package com.bmos.mes.service.plan.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("生产计划模板编辑DTO")
public class PlanTemplateEditDTO {

    @ApiModelProperty("生产计划模板id")
    private Long id;

    @ApiModelProperty("模板批次列表")
    @NotEmpty
    @Valid
    private List<PlanTemplateBatchDTO> batchList;

}
