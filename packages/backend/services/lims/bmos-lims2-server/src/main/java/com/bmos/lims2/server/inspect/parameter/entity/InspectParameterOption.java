package com.bmos.lims2.server.inspect.parameter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据点选项实体类
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@TableName("lm_inspect_parameter_option")
public class InspectParameterOption extends BaseDO {

    /**
     * 数据点id
     */
    private Long dataPointId;

    /**
     * 选项值
     */
    private String optionValue;
} 