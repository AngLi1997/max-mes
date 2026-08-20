package com.bmos.platform.service.equipment.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * APP下显示的设备信息入参
 */
@Getter
@Setter
@ApiModel("APP下显示的设备信息入参")
public class EquipmentAppPageDTO extends BasePage {

    /**
     * 设备code
     */
    @ApiModelProperty("设备code")
    private String code;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String name;

    /**
     * 所选标签id集合
     */
    @ApiModelProperty("所选标签id集合")
    private List<Long> ids;

    @ApiModelProperty("设备id集合")
    private List<Long> infoIdList;

}
