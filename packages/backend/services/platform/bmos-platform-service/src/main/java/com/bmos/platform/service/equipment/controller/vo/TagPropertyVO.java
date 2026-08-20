package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * tag下所有内置属性VO
 */

@Getter
@Setter
@ApiModel("tag下所有内置属性VO")
@NoArgsConstructor
@AllArgsConstructor
public class TagPropertyVO {

    /**
     * 当前tag下所有设备状态VO
     */
    @ApiModelProperty("当前tag下所有设备状态VO")
    private List<EquipmentStatusVO> equipmentStatusVOList;

    /**
     * 当前tag下所有属性VO集合
     */
    @ApiModelProperty("当前tag下所有属性VO集合")
    private List<EquipmentPropertyVO> equipmentPropertyVOList;


    /**
     * 当前tag下所有属性VO集合
     */
    @ApiModelProperty("当前tag下所有数据属性VO集合")
    private List<EquipmentPropertyVO> equipmentDataPropertyVOList;

}
