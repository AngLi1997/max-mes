package com.bmos.adaptor.file.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileVO {
    @ApiModelProperty("文件访问路径")
    private String url;
    @ApiModelProperty("新文件名")
    private String newFilemame;
    @ApiModelProperty("旧文件名")
    private String oldFilename;
    @ApiModelProperty("文件大小")
    private String fileSize;
}
