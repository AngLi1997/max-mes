package com.bmos.lims2.server.report.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("报告模板分页查询参数")
public class ReportTemplatePageQueryDTO extends BasePage {

    @ApiModelProperty("报告名称（模板名称）")
    private String name;

    @ApiModelProperty("物料ID集合（过滤左侧选择的物料）")
    private List<Long> materialIds;
}


