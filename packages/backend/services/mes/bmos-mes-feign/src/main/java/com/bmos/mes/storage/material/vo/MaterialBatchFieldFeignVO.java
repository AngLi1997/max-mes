package com.bmos.mes.storage.material.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 物料批次自定义字段
 */
@Getter
@Setter
public class MaterialBatchFieldFeignVO {

    /**
     * 物料批次自定义字段id
     */
    private Long id;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * 字段类型名称
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
     * 物料批次字段值
     */
    private String fieldValue;

}
