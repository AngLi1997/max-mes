package com.bmos.lims2.server.inspect.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验单自定义字段值实体类
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@TableName("lm_inspection_order_custom_field")
public class InspectionOrderCustomField extends BaseDO {

    /**
     * 检验单ID
     */
    private Long inspectionOrderId;

    /**
     * 字段代码
     */
    private String fieldCode;

    /**
     * 字段所属字典分类代码
     */
    private String dictCode;

    /**
     * 字段显示名称
     */
    private String fieldName;

    /**
     * 字段值
     */
    private String fieldValue;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 排序
     */
    private Integer sort;
}