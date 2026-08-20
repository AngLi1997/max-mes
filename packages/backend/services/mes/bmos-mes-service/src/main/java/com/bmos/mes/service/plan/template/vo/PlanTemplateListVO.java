package com.bmos.mes.service.plan.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("生产计划模板列表VO")
@Data
public class PlanTemplateListVO {

    @ApiModelProperty("生产计划模板id")
    private Long id;

    @ApiModelProperty("生产计划模板名称")
    private String name;

    @ApiModelProperty("确认状态")
    private Boolean confirmed;

}
