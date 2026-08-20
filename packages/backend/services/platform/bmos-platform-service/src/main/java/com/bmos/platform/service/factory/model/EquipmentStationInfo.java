package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 工位与设备绑定关系表，记录工位与设备之间的绑定关系(BpEquipmentStationInfo)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:39:35
 */
@Getter
@Setter
@TableName("bp_equipment_station_info")
public class EquipmentStationInfo extends BaseDO implements Serializable {


    /**
     * 工位id，关联到bp_equipment_station表中的id
     */
    private Long stationId;
    /**
     * 设备id，关联到bp_equipment_info表中的id
     */
    private Long equipmentId;

}

