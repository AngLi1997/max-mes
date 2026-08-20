package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案检验计划实体类
 *
 * @author yigaohui
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("lm_stability_scheme_plan")
public class StabilitySchemePlan extends BaseDO {

    private Long schemeId;

    private Long versionId;

    /** 试验类型 */
    private String experimentType;

    /** 试验储存条件 */
    private String storageCondition;

    /** 整体取样量 */
    private String totalSampleAmount;

    /** 整体取样量单位ID */
    private Long totalSampleUnit;

}
