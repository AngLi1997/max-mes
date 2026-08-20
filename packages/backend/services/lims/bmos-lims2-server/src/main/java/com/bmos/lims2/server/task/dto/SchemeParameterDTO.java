package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 检验方案分析项DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class SchemeParameterDTO {

    /**
     * 方案项目ID
     */
    private Long schemeItemId;

    /**
     * 检验项目ID
     */
    private Long inspectItemId;

    /**
     * 检验项目名称
     */
    private String inspectItemName;

    /**
     * 检验项目编码
     */
    private String inspectItemCode;

    /**
     * 方案分析项ID
     */
    private Long schemeParameterId;

    /**
     * 分析项ID
     */
    private Long parameterId;

    /**
     * 分析项名称
     */
    private String parameterName;

    /**
     * 分析项编码
     */
    private String parameterCode;

    /**
     * 是否可执行
     */
    private Boolean isExecutable;

    /**
     * 是否可报告
     */
    private Boolean isReportable;

    /**
     * 执行方式：LIMS/ELN
     */
    private ExecuteMethodEnum executeMethod;

    /**
     * 方法项ID（来自方案分析项配置：InspectionSchemeParameter.recordItemId）
     */
    private Long recordItemId;

    /**
     * ELN 方法ID（来自方案分析项配置快照：InspectionSchemeParameter.recordId）
     */
    private Long recordId;

    /**
     * ELN 方法版本ID（来自方案分析项配置快照：InspectionSchemeParameter.recordVersionId）
     */
    private Long recordVersionId;

    /**
     * ELN 方法编码（来自方案分析项配置快照：InspectionSchemeParameter.recordCode）
     */
    private String recordCode;
}
