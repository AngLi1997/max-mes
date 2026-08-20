package com.bmos.lims2.server.report.dto;

import com.bmos.lims2.common.enums.ReportTemplateVersionStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 报告模板版本下拉项
 * @Author: yigaohui
 * @Date: 2025/09/08 11:30
 */
@Data
@ApiModel("报告模板版本下拉项")
public class ReportTemplateVersionOptionDTO {

    @ApiModelProperty("版本ID")
    private Long id;

    @ApiModelProperty("模板ID")
    private Long templateId;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("状态")
    private ReportTemplateVersionStatusEnum status;

    @ApiModelProperty("是否默认")
    private Boolean isDefault;

    @ApiModelProperty("显示名称（版本号）")
    private String label;
}


