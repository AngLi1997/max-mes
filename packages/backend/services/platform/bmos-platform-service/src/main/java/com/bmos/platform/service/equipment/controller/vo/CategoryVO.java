package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备分类VO
 */
@Getter
@Setter
@ApiModel("设备分类VO")
public class CategoryVO {

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long id;

    /**
     * 父级id 若没有父级则为0
     */
    @ApiModelProperty(value = "父级分类名称 没有则为null")
    private Long parentName;

    /**
     * 分类编码
     */
    @ApiModelProperty(value = "分类编码")
    private String code;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类编码")
    private String name;

    /**
     * 分类类型
     */
    @ApiModelProperty(value = "分类类型")
    private Integer type;

}
