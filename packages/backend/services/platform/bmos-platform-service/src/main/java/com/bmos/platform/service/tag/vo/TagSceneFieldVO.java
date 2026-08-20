package com.bmos.platform.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标签数据源字段vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 15:30
 */
@Data
@ApiModel("标签数据源字段vo")
public class TagSceneFieldVO {

    /**
     * 字段
     */
    @ApiModelProperty(value = "字段", example = "materialNo")
    private String field;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称", example = "物料件编号")
    private String label;

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型", example = "String")
    private String type;

    /**
     * 示例值
     */
    @ApiModelProperty(value = "示例值", example = "123456")
    private String exampleValue;
}
