package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 设备tag表，记录设备的标签信息(BpEquipmentTag)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:40:02
 */
@Getter
@Setter
@TableName("bp_equipment_tag")
public class EquipmentTag extends BaseDO implements Serializable {

    private Long parentId;

    /**
     * 标签编码
     */
    private String code;
    /**
     * tag名称
     */
    private String name;

    /**
     * 是否内置
     */
    private Boolean embed;

    private String description;
}

