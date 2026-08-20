package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 工位人员绑定关系表，记录工位与用户之间的绑定关系(BpEquipmentStationUser)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:39:48
 */
@Getter
@Setter
@TableName("bp_equipment_station_user")
public class EquipmentStationUser extends BaseDO implements Serializable {

    /**
     * 工位id，关联到bp_equipment_station表中的id
     */
    private Long stationId;
    /**
     * 用户id，标识与工位绑定的用户
     */
    private String userId;

}

