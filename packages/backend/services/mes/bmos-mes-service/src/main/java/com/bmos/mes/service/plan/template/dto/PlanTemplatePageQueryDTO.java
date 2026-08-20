package com.bmos.mes.service.plan.template.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("生产计划模板查询DTO")
@Data
public class PlanTemplatePageQueryDTO extends BasePage {

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("状态(已确认/未确认)")
    private Boolean confirmed;

}
