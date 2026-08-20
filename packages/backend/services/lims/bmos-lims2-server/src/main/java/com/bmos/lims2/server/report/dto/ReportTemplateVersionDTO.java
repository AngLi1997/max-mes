package com.bmos.lims2.server.report.dto;

import com.bmos.lims2.common.enums.ReportTemplateVersionStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("报告模板版本DTO")
public class ReportTemplateVersionDTO {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("模板ID")
    private Long templateId;
    @ApiModelProperty("版本号")
    private String versionNo;
    @ApiModelProperty("状态")
    private ReportTemplateVersionStatusEnum status;
    @ApiModelProperty("是否默认")
    private Boolean isDefault;

    @ApiModelProperty("下载地址（bucket/object）")
    private String path;

    @ApiModelProperty("备注")
    private String remark;
}


