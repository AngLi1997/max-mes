package com.bmos.platform.service.equipment.controller.vo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@ApiModel("工位绑定设备返回vo")
public class EquipmentInfoStationVO {

    @ApiModelProperty("设备id")
    private Long id;


    @ApiModelProperty("设备名称")
    private String name;


    @ApiModelProperty("设备编码")
    private String code;

    @ApiModelProperty("分类id")
    private Long categoryId;

    public String getName() {
        return code + StrUtil.DASHED + name;
    }


}
