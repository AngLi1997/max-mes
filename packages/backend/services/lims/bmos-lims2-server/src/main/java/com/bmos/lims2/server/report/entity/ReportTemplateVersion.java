package com.bmos.lims2.server.report.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.ReportTemplateVersionStatusEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 报告模板版本
 */
@Getter
@Setter
@TableName("lm_report_template_version")
public class ReportTemplateVersion extends BaseDO {

    private Long templateId;
    private String versionNo;
    private ReportTemplateVersionStatusEnum status;
    private Boolean isDefault;
    private String path;
    private String remark;
}


