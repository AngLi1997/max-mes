package com.bmos.lims2.server.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 报告模板
 */
@Getter
@Setter
@TableName("lm_report_template")
public class ReportTemplate extends BaseDO {

    /** 模板名称（全局唯一） */
    private String name;

    /** 检品ID */
    private Long materialId;

    /** 默认版本ID */
    private Long defaultVersionId;

    /** 生效版本ID（冗余） */
    private Long effectiveVersionId;

    /** 生效版本号（冗余） */
    private String effectiveVersionNo;

    /** 备注 */
    private String remark;
}


