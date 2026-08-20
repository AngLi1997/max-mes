package com.bmos.mes.service.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(value = "单个记录项上传返回对象")
public class ItemUploadVO {

    @ApiModelProperty(value = "文件解析字符串")
    private String fileContent;

    @ApiModelProperty(value = "上传单个记录项指令集地址")
    private String itemPath;

    @ApiModelProperty(value = "文档样式")
    private Boolean style;
}
