package com.bmos.lims2.server.inspect.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验项目列表DTO - 用于下拉选择
 * @author system
 */
@Getter
@Setter
@ApiModel("检验项目列表DTO")
public class InspectItemListDTO {

    /**
     * 检验项目id
     */
    @ApiModelProperty(value = "检验项目id")
    private Long id;

    /**
     * 检验项目编码
     */
    @ApiModelProperty(value = "检验项目编码")
    private String code;

    /**
     * 检验项目名称
     */
    @ApiModelProperty(value = "检验项目名称")
    private String name;
}