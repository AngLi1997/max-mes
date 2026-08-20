package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案检验计划时间点实体类
 *
 * @author yigaohui
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("lm_stability_scheme_plan_timepoint")
public class StabilitySchemePlanTimepoint extends BaseDO {

    private Long planId;

    private Long versionId;

    /** 检测时间点数值 */
    private Integer timeValue;

    /** 时间单位（月/年/天/周） */
    private String timeUnit;

    /** 取样量 */
    private String sampleAmount;

    /** 取样量单位 */
    private String sampleUnit;

    /**
     * 是否全选方案配置中的所有分析项
     * true：选择全部；false：按 lm_stability_scheme_plan_timepoint_param 关联表
     */
    private Boolean selectAll;

}
