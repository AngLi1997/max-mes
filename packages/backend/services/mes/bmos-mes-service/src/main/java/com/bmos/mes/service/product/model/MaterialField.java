package com.bmos.mes.service.product.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 生产物料自定义字段表结构
 *
 * @author makejava
 * @since 2024-07-23 13:39:30
 */
@Getter
@Setter
@TableName("bm_material_field")
public class MaterialField extends BaseDO {

    /**
     * 字典类型
     */
    private String fieldType;
    /**
     * 字典类型名称
     */
    private String fieldTypeName;
    /**
     * 字段
     */
    private String field;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段值
     */
    private String fieldValue;
    /**
     * 生产物料id 表bp_material的主键id
     */
    private Long materialId;

}

