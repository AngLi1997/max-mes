package com.bmos.lims2.server.inspect.parameter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分析项数据点实体类
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@TableName("lm_inspect_parameter_data_point")
public class InspectParameterDataPoint extends BaseDO {

    /**
     * 分析项id
     */
    private Long parameterId;

    /**
     * 数据点名称
     */
    private String name;

    /**
     * 数据点类型
     */
    private AnalyzeResultTypeEnum resultType;

    /**
     * 标准规定
     */
    private String standard;

    /**
     * 时间类型显示格式（仅当resultType为TIME时有效），例如：yyyy-MM-dd HH:mm:ss
     */
    private String timeFormat;

    private String dateStyle;


    /**
     * 是否报告显示
     */
    private Boolean reportDisplay;

    /**
     * 选项列表（仅当resultType为OPTION时有效）
     */
    @TableField(exist = false)
    private List<InspectParameterOption> options;

    /**
     * 趋势线列表（仅当resultType为NUMBER时有效）
     */
    @TableField(exist = false)
    private List<InspectParameterTrend> trends;
} 