package com.bmos.lims2.server.inspect.parameter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分析项实体类
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@TableName("lm_inspect_parameter")
public class InspectParameter extends BaseDO {

    /**
     * 分析项编码
     */
    private String code;

    /**
     * 分析项名称
     */
    private String name;

    /**
     * 标准规定模版
     */
    private String standard;

    /**
     * 数据点列表
     */
    @TableField(exist = false)
    private List<InspectParameterDataPoint> dataPoints;
}

