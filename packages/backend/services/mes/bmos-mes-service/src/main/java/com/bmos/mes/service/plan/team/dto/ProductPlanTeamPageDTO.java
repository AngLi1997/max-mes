package com.bmos.mes.service.plan.team.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("ProductPlanTeamPageDTO:生产计划班组分页列表查询条件DTO")
public class ProductPlanTeamPageDTO extends BasePage {
    @ApiModelProperty("班组名称")
    private String name;

    @ApiModelProperty("班组编码")
    private String code;

    @ApiModelProperty(hidden = true)
    private List<Long> deptIds;
}
