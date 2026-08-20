package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * 设备-点位关联信息(BpEquipmentAcquisition)实体类
 *
 * @author makejava
 * @since 2024-04-22 11:36:43
 */
@Getter
@Setter
@ToString
@TableName("bp_equipment_acquisition")
public class EquipmentAcquisition extends BaseDO implements Serializable {
    private static final long serialVersionUID = -62416223803092050L;
    /**
     * 设备id
     */
    private Long equipmentId;
    /**
     * 设备编码
     */
    private String equipmentCode;
    /**
     * 采集点id
     */
    private Long acquisitionPointId;
    /**
     * 采集点编码
     */
    private String acquisitionPointCode;


    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public Long getAcquisitionPointId() {
        return acquisitionPointId;
    }

    public void setAcquisitionPointId(Long acquisitionPointId) {
        this.acquisitionPointId = acquisitionPointId;
    }

    public String getAcquisitionPointCode() {
        return acquisitionPointCode;
    }

    public void setAcquisitionPointCode(String acquisitionPointCode) {
        this.acquisitionPointCode = acquisitionPointCode;
    }

}

