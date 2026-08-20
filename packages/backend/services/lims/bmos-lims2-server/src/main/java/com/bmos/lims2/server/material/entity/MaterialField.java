package com.bmos.lims2.server.material.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("lm_inspect_material_field")
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
     * 检品id 表lm_basic_products的主键id
     */
    private Long materialId;



}
