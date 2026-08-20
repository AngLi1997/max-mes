package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("新增报告模板DTO（包含初始版本与数据权限）")
public class ReportTemplateSaveDTO {

    @ApiModelProperty("模板名称（全局唯一）")
    @NotBlank
    private String name;

    @ApiModelProperty("检品ID")
    @NotNull
    private Long materialId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("初始版本号（同一模板下唯一）")
    @NotBlank
    private String versionNo;

    @ApiModelProperty("授权部门ID集合")
    @NotEmpty
    private List<Long> deptIds;

    @ApiModelProperty("模板文件路径")
    @NotBlank
    private String path;
}


