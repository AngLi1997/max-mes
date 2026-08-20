package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;

/**
 * (BpFactoryRoomEnvProperty)实体类
 *
 * @author makejava
 * @since 2024-12-30 10:04:53
 */
@TableName("bp_factory_room_env_property")
@Data
public class FactoryRoomEnvProperty extends BaseDO {
    private static final long serialVersionUID = 593776516580148780L;
    /**
     * 房间id
     */
    private Long roomId;

    private Long equipmentId;
    /**
     * 设备数据编码
     */
    private String equipmentDataPropertyCode;

    private String envPropertyCode;


}

