package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel("查询工位信息返回vo")
public class ChoiceBoxVO {

    @ApiModelProperty(value = "key")
    private String label;

    @ApiModelProperty(value = "value")
    private Long value;
}
