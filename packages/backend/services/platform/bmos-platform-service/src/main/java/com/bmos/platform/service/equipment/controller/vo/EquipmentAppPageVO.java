package com.bmos.platform.service.equipment.controller.vo;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.platform.facade.equipment.enums.EquipmentStatusCodeEnum;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * APP下显示的设备信息VO
 */
@Getter
@Setter
@ApiModel("APP下显示的设备信息VO")
public class EquipmentAppPageVO {

    /**
     * 设备id
     */
    @ApiModelProperty("设备id")
    private Long id;

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
     * 设备状态
     */
    @ApiModelProperty("设备状态")
    private Integer status;

    /**
     * 设备状态名称
     */
    @ApiModelProperty("设备状态名称")
    private String statusName;

    /**
     * 设备有效期
     */
    @ApiModelProperty("设备有效期")
    private LocalDateTime expireDateTime;



    /**
     * 数采平台
     */
    @ApiModelProperty(value = "数采平台")
    private AcquisitionPlatformEnum acquisitionPlatform;

    public String getStatusName() {
        EquipmentStatusCodeEnum enumList = EquipmentStatusCodeEnum.getByCode(status);
        return ObjectUtil.isEmpty(enumList) ? "" : enumList.getDesc();
    }

}
