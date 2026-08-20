package com.bmos.lims2.server.inspect.parameter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 数据点趋势线实体类
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@TableName("lm_inspect_parameter_trend")
public class InspectParameterTrend extends BaseDO {

    /**
     * 数据点id
     */
    private Long dataPointId;

    /**
     * 范围名称
     */
    private String rangeName;

    /**
     * 最小值
     */
    private BigDecimal minValue;

    /**
     * 最小值比较运算符
     */
    private CompareOperatorEnum minOperator;

    /**
     * 最大值
     */
    private BigDecimal maxValue;

    /**
     * 最大值比较运算符
     */
    private CompareOperatorEnum maxOperator;
} 