package com.bmos.lims2.web.report.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("报告模板分页查询参数")
public class ReportTemplatePageReqVO extends BasePage {

    @ApiModelProperty("报告名称（模板名称）")
    private String name;

    @ApiModelProperty("物料ID集合（过滤左侧选择的物料）")
    private List<Long> materialIds;

    @ApiModelProperty("物料分类ID（仅传分类时由后端解析启用检品ID集；为空则不按分类过滤）")
    private Long categoryId;

    @ApiModelProperty("物料ID（与分类同时传时以物料ID优先）")
    private Long materialId;
}
