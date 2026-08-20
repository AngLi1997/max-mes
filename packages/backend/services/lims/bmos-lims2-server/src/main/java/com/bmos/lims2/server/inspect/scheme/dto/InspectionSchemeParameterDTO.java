package com.bmos.lims2.server.inspect.scheme.dto;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import lombok.Data;

import java.util.List;

/**
 * 检验方案分析项配置DTO
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeParameterDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的方案ID
     */
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    private Long versionId;

    private Long packageId;

    private Long inspectItemId;

    /**
     * 关联的检验项目配置ID
     */
    private Long itemConfigId;

    /**
     * 分析项ID
     */
    private Long parameterId;

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

    /**
     * 记录项ID（bm_batch_record_item.id）
     */
    private Long recordItemId;

    /**
     * 分析方法名称（优先standard，其次version，再次code）
     */
    private String recordName;

    private String recordVersion;

    /**
     * 分析项名称
     */
    private String parameterName;

    /**
     * 分析项编码
     */
    private String parameterCode;

    /**
     * 标准规定
     */
    private String standardRule;

    /**
     * 是否报告项
     */
    private Boolean isReportable;

    /**
     * 是否可执行
     */
    private Boolean isExecutable;

    /**
     * 最终表达式
     */
    private String finalExpression;

    /**
     * 执行方式：LIMS/ELN
     */
    private ExecuteMethodEnum executeMethod;

    /**
     * 数据点配置列表
     */
    private List<InspectionSchemeDataPointDTO> dataPoints;

    /**
     * 判定配置列表
     */
    private List<InspectionSchemeJudgmentDTO> judgments;

    /**
     * 判定配置一致性标识
     */
    private Boolean judgmentConfigError;
    private Boolean judgmentDataPointDeleted;
    private Boolean judgmentDataPointBindingMissing;
    private Boolean judgmentDataPointTypeChanged;
    private Boolean judgmentDataPointOptionInvalid;
}