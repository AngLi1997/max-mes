package com.bmos.mes.service.plan.document.service.dto;

import com.bmos.mes.common.enums.plan.TemplateVersionOperateTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录模板操作日志生成DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class BatchTemplateLogSaveDTO {

    /**
     * 操作类型
     */
    private TemplateVersionOperateTypeEnum operateType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 模板版本id
     */
    private Long batchTemplateVersionId;

    /**
     * 模板minio路径
     */
    private String path;

}
