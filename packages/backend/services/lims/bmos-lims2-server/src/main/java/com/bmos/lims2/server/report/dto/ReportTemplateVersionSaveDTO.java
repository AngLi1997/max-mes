package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 新增报告模板版本 DTO
 * @Author: yigaohui
 * @Date: 2025/09/03 00:00
 */
@Getter
@Setter
@ApiModel("新增报告模板版本DTO")
public class ReportTemplateVersionSaveDTO {

    @ApiModelProperty(value = "模板ID", required = true)
    @NotNull
    private Long templateId;

    @ApiModelProperty(value = "版本号", required = true, example = "v1.1.0")
    @NotBlank
    private String versionNo;

    @ApiModelProperty(value = "版本文件路径（存储对象Key）", required = true)
    @NotBlank
    private String path;

    @ApiModelProperty("备注")
    private String remark;
}


