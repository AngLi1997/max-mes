package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报告自定义字段索引DTO（来自平台字典）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("报告自定义字段索引")
public class ReportCustomFieldIndexDTO {

    @ApiModelProperty("字典分类code（如 MaterialCustomFields）")
    private String categoryCode;

    @ApiModelProperty("字典分类名称")
    private String categoryName;

    @ApiModelProperty("字段code（dictValue）")
    private String fieldCode;

    @ApiModelProperty("字段名称（dictLabel）")
    private String fieldName;

    @ApiModelProperty("模板占位符")
    private String indexPlaceholder;
}
