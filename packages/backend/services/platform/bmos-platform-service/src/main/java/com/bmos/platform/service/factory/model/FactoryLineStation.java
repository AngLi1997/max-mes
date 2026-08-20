package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 产线与工位的直接绑定的关系(BpFactoryLineStation)实体类
 *
 * @author makejava
 * @since 2024-05-21 17:05:46
 */
@Getter
@Setter
@TableName("bp_factory_line_station")
public class FactoryLineStation extends BaseDO {

    /**
     * 产线id，关联到bp_factory_line表中的id
     */
    private Long lineId;
    /**
     * 工位id，关联到bp_factory_room表中的id
     */
    private Long stationId;

}

