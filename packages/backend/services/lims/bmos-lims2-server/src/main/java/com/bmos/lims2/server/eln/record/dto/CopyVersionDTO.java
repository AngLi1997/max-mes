package com.bmos.lims2.server.eln.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel(value = "复制版本DTO")
public class CopyVersionDTO {

    @ApiModelProperty(value = "版本号")
    @NotBlank
    private String version;

    @ApiModelProperty(value = "文件地址")
    private String filePath;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "复制版本id")
    @NotNull
    private Long versionOldId;

    @ApiModelProperty(value = "记录id")
    @NotNull
    private Long recordId;

    @ApiModelProperty("新的版本id")
    private Long id;
}
