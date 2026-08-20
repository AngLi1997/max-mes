package com.bmos.lims2.server.inspect.scheme.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验方案数据点配置实体类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Getter
@Setter
@TableName("lm_inspection_scheme_data_point")
public class InspectionSchemeDataPoint extends BaseDO {

    /**
     * 关联的方案ID
     */
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    private Long versionId;

    private Long packageId;

    private Long inspectItemId;

    private Long parameterId;

    /**
     * 关联的分析项配置ID
     */
    private Long parameterConfigId;

    /**
     * 原始数据点ID
     */
    private Long dataPointId;

    /**
     * 数据点名称
     */
    private String name;

    /**
     * 数据点类型：NUMBER-数值, TEXT-文本, OPTION-选项, TIME-时间
     */
    private DataPointTypeEnum pointType;

    /**
     * 趋势线配置(JSON)
     */
    private String trendLineConfig;

    /**
     * 选项配置(JSON)
     */
    private String options;

    /**
     * 时间类型显示格式（仅当pointType为TIME时有效），例如：yyyy-MM-dd HH:mm:ss
     */
    private String timeFormat;
    private String dateStyle;

    /**
     * 时间类型时长舍入方式：true-向上舍入；false-向下舍入（仅当pointType为TIME时有效）
     */
    @TableField("rounding_up")
    private Boolean roundingUp;


    /**
     * 是否报告显示
     */
    private Boolean reportDisplay;

    /**
     * 记录id（绑定记录组件用）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long recordId;

    /**
     * 记录版本id（绑定记录组件用）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long recordVersionId;

    /**
     * 记录组件id（bm_batch_record_component.id）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long componentId;

    /**
     * 记录项id（bm_batch_record_item.id）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long recordItemId;

    /**
     * 字段id（fieldId，对应记录组件字段）
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long fieldId;

} 