package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案检验计划时间点分析项关联实体
 * 替代原 param_refs JSON 列，每条分析项引用存一行
 *
 * @author yigaohui
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("lm_stability_scheme_plan_timepoint_param")
public class StabilitySchemePlanTimepointParam extends BaseDO {

    /** 时间点ID */
    private Long timepointId;

    /** 计划ID（冗余） */
    private Long planId;

    /** 版本ID（冗余） */
    private Long versionId;

    /** 分析项配置ID（lm_stability_scheme_parameter.id） */
    private Long parameterConfigId;

    /** 原始分析项ID（冗余） */
    private Long parameterId;

    /** 分析项编码（冗余） */
    private String parameterCode;

    /** 检验项目配置ID（lm_stability_scheme_item.id）（冗余） */
    private Long itemConfigId;

    /** 检验项目ID（冗余） */
    private Long inspectItemId;

    /** 检验项目编码（冗余） */
    private String inspectItemCode;
}
