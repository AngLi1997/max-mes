package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案数据点配置实体类
 */
@Getter
@Setter
@TableName("lm_stability_scheme_data_point")
public class StabilitySchemeDataPoint extends BaseDO {

    private Long schemeId;

    private Long versionId;

    /**
     * 检验项目配置ID（lm_stability_scheme_item.id）
     */
    private Long itemConfigId;

    /**
     * 分析项配置ID（lm_stability_scheme_parameter.id）
     */
    private Long parameterConfigId;

    /**
     * 原始分析项ID
     */
    private Long parameterId;

    /**
     * 原始数据点ID
     */
    private Long dataPointId;

    private String name;

    private DataPointTypeEnum pointType;

    private String trendLineConfig;

    private String options;

    private String timeFormat;

    private String dateStyle;

    @TableField("rounding_up")
    private Boolean roundingUp;

    private Boolean reportDisplay;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long recordId;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long recordVersionId;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long componentId;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long recordItemId;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long fieldId;
}
