package com.bmos.lims2.web.report.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ApiModel("更新报告模板版本文件请求VO")
public class ReportVersionFileUpdateReqVO {

    @ApiModelProperty("文件桶")
    @NotBlank
    private String fileBucket;

    @ApiModelProperty("文件对象键")
    @NotBlank
    private String fileObject;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件MD5")
    private String fileMd5;
}


