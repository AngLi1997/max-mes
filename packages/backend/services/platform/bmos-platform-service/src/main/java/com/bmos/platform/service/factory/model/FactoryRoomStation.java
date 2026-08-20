package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 房间与工位的绑定关系(BpFactoryRoomStation)实体类
 *
 * @author makejava
 * @since 2024-05-21 10:17:22
 */
@Getter
@Setter
@TableName("bp_factory_room_station")
public class FactoryRoomStation extends BaseDO {

    /**
     * 房间id，关联到bp_factory_room表中的id
     */
    private Long roomId;
    /**
     * 工位id，关联到bp_equipment_station表中的id
     */
    private Long stationId;

}

