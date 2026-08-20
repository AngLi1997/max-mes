package com.bmos.mes.service.plan.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("生产计划模板详情VO")
@Data
public class PlanTemplateDetailVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("模板批次列表")
    private List<PlanTemplateDetailBatchVO> templateBatchList;

}
