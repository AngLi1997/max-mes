package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import lombok.Data;

import java.util.List;

/**
 * 检验方案分析项配置保存请求DTO
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeParameterSaveDTO {


    /**
     * 检验项目id
     */
    private Long inspectItemId;

    /**
     * 分析项ID
     */
    private Long parameterId;

    /**
     * 标准规定
     */
    private String standardRule;


    /**
     * 分析方法ID
     */
    private Long recordId;

    /**
     * 分析方法编码
     */
    private String recordCode;

    /**
     * 分析方法版本ID
     */
    private Long recordVersionId;


    private Long recordItemId;
    /**
     * 是否报告项
     */
    private Boolean isReportable;

    /**
     * 是否可执行
     */
    private Boolean isExecutable;

    /**
     * 数据点配置列表
     */
    private List<InspectionSchemeDataPointSaveDTO> dataPoints;


    /**
     * 最终判定表达式（由前端传入）
     */
    private String finalExpression;

    /**
     * 判定配置列表
     */
    private List<InspectionSchemeJudgmentSaveDTO> judgments;

    /**
     * 执行方式：LIMS/ELN
     */
    private ExecuteMethodEnum executeMethod;
}