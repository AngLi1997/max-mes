package com.bmos.mes.service.preparation.input.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 配液单投料信息
 */
@Data
@ApiModel("配液单投料信息")
public class PreparationInputPlanVO {

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产指令单id", example = "1")
    private Long productPlanId;

    /**
     * 配料单id
     */
    @ApiModelProperty(value = "配料单id", example = "1")
    private Long preparePlanId;

    /**
     * 配料单名称
     */
    @ApiModelProperty(value = "配料单名称", example = "配料单名称")
    private String preparePlanName;

    /**
     * 配液单待投料列表
     */
    @ApiModelProperty("配液单待投料列表")
    private List<PreparationInputRecordVO> inputList = new ArrayList<>();
}
