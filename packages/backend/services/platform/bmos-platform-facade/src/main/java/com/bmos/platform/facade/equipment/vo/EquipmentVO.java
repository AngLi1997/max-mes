package com.bmos.platform.facade.equipment.vo;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.platform.facade.equipment.enums.AcquisitionPlatformEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取删除、停用的设备信息
 */
@Getter
@Setter
@ApiModel(value = "获取删除、停用的设备信息")
public class EquipmentVO {

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private Long id;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String name;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 启停状态
     */
    private Boolean enable;

    /**
     * 是否删除
     */
    private Boolean deleted;



    /**
     * 数采平台
     * {@link AcquisitionPlatformEnum}
     */
    @ApiModelProperty(value = "数采平台")
    private String acquisitionPlatform;

    public AcquisitionPlatformEnum getAcquisitionPlatformEnum() {
        return CommonEnum.getEnumByValue(AcquisitionPlatformEnum.class, acquisitionPlatform);
    }

    public void setAcquisitionPlatform(AcquisitionPlatformEnum acquisitionPlatform) {
        if (acquisitionPlatform != null){
            this.acquisitionPlatform = acquisitionPlatform.getValue();
        }
    }

}
