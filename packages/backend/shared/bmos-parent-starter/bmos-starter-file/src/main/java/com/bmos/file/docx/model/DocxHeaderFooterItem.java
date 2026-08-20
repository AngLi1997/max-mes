package com.bmos.file.docx.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/20 10:50
 */
@Data
@ApiModel("页眉/页脚")
public class DocxHeaderFooterItem {

    /**
     * 页眉/页脚内容
     */
    @ApiModelProperty(value = "页眉/页脚内容")
    private String content;

    /**
     * 页码水平对齐方式
     */
    @ApiModelProperty(value = "页码水平对齐方式")
    private Integer pageCodeHorizontalAlignment;
}
