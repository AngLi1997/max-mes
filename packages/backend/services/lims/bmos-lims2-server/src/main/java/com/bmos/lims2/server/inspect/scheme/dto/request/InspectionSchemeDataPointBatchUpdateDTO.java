package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 检验方案数据点批量更新DTO
 *
 * @author yigaohui
 * @since 2025/01/29 20:00
 */
@Data
public class InspectionSchemeDataPointBatchUpdateDTO {

    @ApiModelProperty("数据点配置ID（修改时需要）")
    private Long dataPointConfigId;

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
     * 数据点类型：NUMBER-数值类型, TEXT-文本类型, OPTION-选项类型
     */
    private DataPointTypeEnum pointType;

    private String timeFormat;

    private String dateStyle;

    /**
     * 时间类型时长舍入方式：true-向上；false-向下
     */
    private Boolean roundingUp;


    /**
     * 趋势线配置(JSON)
     */
    private String trendLineConfig;

    /**
     * 选项配置(JSON)
     */
    private String options;

    /**
     * 是否报告显示
     */
    private Boolean reportDisplay;

    /**
     * 最终判定表达式（由多个判定条件组合而成）
     */
    private String finalExpression;

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
