package com.bmos.platform.service.equipment.service.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 设备-点位关联信息(BpEquipmentAcquisition)实体类
 *
 * @author makejava
 * @since 2024-04-22 11:36:43
 */
@Getter
@Setter
@ToString
public class EquipmentAcquisitionDTO extends BaseDO implements Serializable {
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


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;

}

