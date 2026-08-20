package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 产线与房间的绑定关系(BpFactoryLineRoom)实体类
 *
 * @author makejava
 * @since 2024-05-21 17:05:26
 */
@Getter
@Setter
@TableName("bp_factory_line_room")
public class FactoryLineRoom extends BaseDO {

    /**
     * 产线id，关联到bp_factory_line表中的id
     */
    private Long lineId;
    /**
     * 房间id，关联到bp_factory_room表中的id
     */
    private Long roomId;
}

