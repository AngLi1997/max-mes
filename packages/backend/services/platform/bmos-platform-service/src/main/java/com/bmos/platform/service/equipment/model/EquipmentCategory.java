package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 设备分类表，记录设备的分类信息(BpEquipmentCategory)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:35:47
 */
@Getter
@Setter
@TableName("bp_equipment_category")
public class EquipmentCategory extends BaseDO implements Serializable {

    /**
     * 设备类别编码
     */
    private String code;
    /**
     * 设备类别名称
     */
    private String name;
    /**
     * 当前模型上级，若没有上级则为0
     */
    private Long parentId;

    /**
     * 级别id
     */
    private String treeCode;

}

