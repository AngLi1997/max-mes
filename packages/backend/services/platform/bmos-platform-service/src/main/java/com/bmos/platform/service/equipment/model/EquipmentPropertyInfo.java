package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 设备与属性表，记录设备的属性信息(BpEquipmentPropertyInfo)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:39:17
 */
@Getter
@Setter
@TableName("bp_equipment_property_info")
public class EquipmentPropertyInfo extends BaseDO implements Serializable {

    /**
     * 设备id，关联到bp_equipment_info表中的id
     */
    private Long equipmentId;
    /**
     * 属性类型，1-设备状态，2-设备属性
     */
    private Integer propertyType;
    /**
     * 属性code，用于唯一标识设备属性
     */
    private String propertyCode;
    /**
     * 属性名称
     */
    private String name;
    /**
     * 当前设备属性的默认值
     * 状态类的配置：这个值为有效时长，目前可以设置天、时、分、秒，以","隔开
     * 信息类的配置：这个值为信息字段输入的值
     * 数据类的配置：这个值对应采集点位id
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String value;
    /**
     * 当前设备属性的实际值
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String actualValue;
    /**
     * 当前完成状态
     */
    private Boolean finishStatus;
    /**
     * 是否内置
     */
    private Boolean embed;
    /**
     * 是否必填
     */
    private Boolean required;

}

