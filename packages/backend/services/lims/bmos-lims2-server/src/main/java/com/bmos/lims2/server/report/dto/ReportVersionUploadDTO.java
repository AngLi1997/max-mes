package com.bmos.lims2.server.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("报告模板版本上传DTO")
public class ReportVersionUploadDTO {

    @ApiModelProperty("版本ID")
    @NotNull
    private Long versionId;

    @ApiModelProperty("文件桶")
    private String fileBucket;

    @ApiModelProperty("文件对象键")
    private String fileObject;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件MD5")
    private String fileMd5;
}


