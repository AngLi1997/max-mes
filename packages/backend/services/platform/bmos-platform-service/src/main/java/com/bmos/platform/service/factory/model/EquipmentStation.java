package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 设备工位表，记录设备工位的相关信息(BpEquipmentStation)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:39:24
 */
@Getter
@Setter
@TableName("bp_equipment_station")
public class EquipmentStation extends BaseDO implements Serializable {

    /**
     * 工位code，用于唯一标识工位
     */
    private String code;
    /**
     * 工位名称，对工位的描述性文字
     */
    private String name;
    /**
     * 工位描述，提供额外的工位信息
     */
    private String description;
    /**
     * 启停状态，表示工位是否启用
     */
    private Boolean enable;
    /**
     * 模型id，关联到bp_equipment_module表中的id
     */
    private Long moduleId;
    /**
     * 使用次数
     */
    private Integer useCount;

}

