package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("报告模板DTO")
public class ReportTemplateDTO {

    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("模板名称")
    private String name;
    @ApiModelProperty("检品ID")
    private Long materialId;
    @ApiModelProperty("默认版本ID")
    private Long defaultVersionId;
    @ApiModelProperty("生效版本ID")
    private Long effectiveVersionId;
    @ApiModelProperty("生效版本号")
    private String effectiveVersionNo;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("模板版本列表")
    private List<ReportTemplateVersionDTO> versions;

    @ApiModelProperty("检品名称")
    private String materialName;
    @ApiModelProperty("检品编码")
    private String materialCode;
    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("报告绑定的方案ID列表")
    private java.util.List<Long> schemeIdList;

    @ApiModelProperty("是否存在有效报告（针对当前检验单）")
    private Boolean hasValidReport;

    @ApiModelProperty("最新的有效报告信息（针对当前检验单）")
    private ReportGeneratedDTO validReport;
}


