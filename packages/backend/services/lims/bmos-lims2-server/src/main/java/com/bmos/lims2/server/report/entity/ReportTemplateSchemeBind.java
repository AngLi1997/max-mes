package com.bmos.lims2.server.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 报告模板与检验方案版本绑定
 */
@Getter
@Setter
@TableName("lm_report_template_scheme_bind")
public class ReportTemplateSchemeBind extends BaseDO {

    private Long templateId;
    private Long schemeId;
    private String remark;
}


