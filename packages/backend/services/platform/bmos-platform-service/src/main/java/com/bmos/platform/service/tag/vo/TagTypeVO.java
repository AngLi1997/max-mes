package com.bmos.platform.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标签类型vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:14
 */
@Data
@ApiModel("标签类型")
public class TagTypeVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 标签类型名称
     */
    @ApiModelProperty(value = "标签类型名称", example = "原辅包标签")
    private String tagTypeName;
}
