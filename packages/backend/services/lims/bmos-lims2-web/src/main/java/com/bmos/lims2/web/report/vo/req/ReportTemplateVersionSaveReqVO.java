package com.bmos.lims2.web.report.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 新增报告模板版本请求VO
 * @Author: yigaohui
 * @Date: 2025/09/03 00:00
 */
@Getter
@Setter
@ApiModel("新增报告模板版本请求VO")
public class ReportTemplateVersionSaveReqVO {

    @ApiModelProperty(value = "版本号", required = true, example = "v1.1.0")
    @NotBlank
    private String versionNo;

    @ApiModelProperty(value = "版本文件路径（存储对象Key）", required = true)
    @NotBlank
    private String path;

    @ApiModelProperty("备注")
    private String remark;
}


