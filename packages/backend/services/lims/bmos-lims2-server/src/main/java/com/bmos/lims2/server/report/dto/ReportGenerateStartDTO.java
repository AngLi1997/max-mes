package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("报告生成开始DTO")
public class ReportGenerateStartDTO {

    @ApiModelProperty("模板版本ID")
    @NotNull
    private Long templateVersionId;

    @ApiModelProperty("检验单ID（样品审核已通过）")
    @NotNull
    private Long inspectionOrderId;

    @ApiModelProperty("可选：指定使用的检验方案版本ID（不传则使用检验单上的版本ID）")
    private Long schemeVersionId;

    @ApiModelProperty("选中的操作规程版本ID列表（检验依据）")
    private List<Long> selectedOperateVersionIds;
}


