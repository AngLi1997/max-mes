package com.bmos.mes.service.preparation.produce.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 配液产出的配液单信息VO
 */
@Data
@ApiModel("配液产出的配液单信息VO")
public class PreparationProducePlanVO {

    /**
     * 配液单id
     */
    @ApiModelProperty(value = "配液单id")
    private Long id;

    /**
     * 配液单名称
     */
    @ApiModelProperty(value = "配液单名称")
    private String name;

}
