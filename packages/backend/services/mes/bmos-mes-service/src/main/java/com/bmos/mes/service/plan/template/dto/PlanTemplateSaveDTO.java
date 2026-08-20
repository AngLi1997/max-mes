package com.bmos.mes.service.plan.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel("生产计划模板保存DTO")
@Data
public class PlanTemplateSaveDTO {

    @ApiModelProperty("生产计划模板名称")
    @NotBlank
    private String name;

    @ApiModelProperty("模板批次列表")
    @NotEmpty
    @Valid
    private List<PlanTemplateBatchDTO> batchList;

}
