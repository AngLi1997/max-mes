package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * (BpEquipmentTagProperty)实体类
 *
 * @author makejava
 * @since 2024-04-23 11:55:53
 */
@Getter
@Setter
@TableName("bp_equipment_tag_property")
public class EquipmentTagProperty extends BaseDO implements Serializable {

    /**
     * 属性code
     */
    private String code;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 是否内置
     */
    private Boolean embed;

    /**
     * 属性类型（1-设备状态，2-设备属性）
     */
    private Integer propertyType;

    /**
     * 标签id
     */
    private Long tagId;
}

