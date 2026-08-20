package com.bmos.mes.service.plan.team.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ProductPlanTeamListDTO:生产计划班组列表查询条件DTO")
public class ProductPlanTeamListDTO {
    @ApiModelProperty("班组名称")
    private String name;

    @ApiModelProperty("班组编码")
    private String code;

    @ApiModelProperty("状态")
    private String status;
}
