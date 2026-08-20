package com.bmos.platform.facade.equipment.vo;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.platform.facade.equipment.enums.AcquisitionPlatformEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * 设备信息
 */
@Getter
@Setter
public class EquipmentInfoFeignVO {

    /**
     * 设备id
     */
    private Long id;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备有限期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate expireDate;
    /**
     * 设备是否可用状态
     * com.bmos.platform.service.equipment.enums.EquipmentStatusEnum
     */
    private Integer status;

    /**
     * tag的名称集合
     */
    private List<String> tagNames;

    private List<TagFeignVO> equipmentTagDataList;

    /**
     * 工位id
     */
    private List<Long> stationIdList;

    /**
     * 设备标签下的各个状态
     */
    private List<EquipmentStatusFeignVO> statusPropertyList;

    /**
     * 设备标签下的各个属性
     */
    private List<EquipmentPropertyFeignVO> infoPropertyList;

    /**
     * 设备标签下的各个属性
     */
    private List<EquipmentPropertyAcquisitionPointFeignVO> dataPropertyList;



    /**
     * 数采平台
     */
    @ApiModelProperty(value = "数采平台")
    private String acquisitionPlatform;

    public AcquisitionPlatformEnum getAcquisitionPlatformEnum() {
        return CommonEnum.getEnumByValue(AcquisitionPlatformEnum.class, acquisitionPlatform);
    }

    public void setAcquisitionPlatform(AcquisitionPlatformEnum acquisitionPlatform) {
        if (acquisitionPlatform == null){
            this.acquisitionPlatform = null;
        } else {
            this.acquisitionPlatform = acquisitionPlatform.getValue();
        }
    }

    public void setAcquisitionPlatform(String acquisitionPlatform) {
        this.acquisitionPlatform = acquisitionPlatform;
    }

}
