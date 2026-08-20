package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import lombok.Data;


/**
 * 检验方案数据点配置保存请求DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeDataPointSaveDTO {


    private Long id;

    /**
     * 检验项目id
     */
    private Long inspectItemId;
    /**
     * 分析项id
     */
    private Long inspectParameterId;

    /**
     * 原始数据点ID
     */
    private Long dataPointId;

    /**
     * 数据点名称
     */
    private String name;

    /**
     * 数据点类型：NUMBER-数值类型, TEXT-文本类型, OPTION-选项类型
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
     * 时间类型显示格式（仅当pointType为TIME时有效）
     */
    private String timeFormat;

    private String dateStyle;

    /**
     * 时间类型时长舍入方式：true-向上；false-向下
     */
    private Boolean roundingUp;


    /**
     * 是否报告显示
     */
    private Boolean reportDisplay;

    /**
     * 记录id（绑定记录组件用）
     */
    private Long recordId;

    /**
     * 记录版本id（绑定记录组件用）
     */
    private Long recordVersionId;

    /**
     * 记录组件id（bm_batch_record_component.id）
     */
    private Long componentId;

    /**
     * 记录项id（bm_batch_record_item.id）
     */
    private Long recordItemId;

    /**
     * 字段id（fieldId，对应记录组件字段）
     */
    private Long fieldId;

} 