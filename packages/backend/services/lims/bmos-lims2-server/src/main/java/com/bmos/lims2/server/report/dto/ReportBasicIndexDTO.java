package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 报告基础信息索引DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("报告基础信息索引")
public class ReportBasicIndexDTO {

    @ApiModelProperty("字段显示名称")
    private String fieldName;

    @ApiModelProperty("模板占位符")
    private String indexPlaceholder;
}
