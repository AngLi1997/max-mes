package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 设备与标签表，记录设备与标签之间的关系(BpEquipmentTagInfo)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:40:09
 */
@Getter
@Setter
@TableName("bp_equipment_tag_info")
public class EquipmentTagInfo extends BaseDO implements Serializable {

    /**
     * 设备id，关联到bp_equipment_info表中的id
     */
    private Long equipmentId;
    /**
     * 标签id，关联到bp_equipment_tag表中的id
     */
    private Long tagId;

}

