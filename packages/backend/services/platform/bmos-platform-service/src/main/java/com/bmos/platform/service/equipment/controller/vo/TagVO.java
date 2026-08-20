package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 标签VO
 */
@Getter
@Setter
@ApiModel("标签VO")
public class TagVO {

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Long id;

    /**
     * 标签code
     */
    @ApiModelProperty("标签code")
    private String code;

    /**
     * 标签id
     */
    @ApiModelProperty("标签名称")
    private String name;

}
