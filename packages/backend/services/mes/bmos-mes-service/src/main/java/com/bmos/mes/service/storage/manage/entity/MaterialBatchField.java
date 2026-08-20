package com.bmos.mes.service.storage.manage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * (BmMaterialBatchField)实体类
 * 物料批次自定义字段
 * @author makejava
 * @since 2024-07-23 14:42:23
 */
@Getter
@Setter
@TableName("bm_material_batch_field")
public class MaterialBatchField extends BaseDO {

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
     * 物料批次id 表bm_storage_material_batch的主键id
     */
    private Long materialBatchId;

}

