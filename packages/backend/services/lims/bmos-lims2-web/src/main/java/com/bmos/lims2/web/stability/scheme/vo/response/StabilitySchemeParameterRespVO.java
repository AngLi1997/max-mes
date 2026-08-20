package com.bmos.lims2.web.stability.scheme.vo.response;

import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 稳定性方案分析项配置响应VO
 */
@Data
@ApiModel("稳定性方案分析项配置响应")
public class StabilitySchemeParameterRespVO {

    @ApiModelProperty("分析项配置ID")
    private Long parameterConfigId;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("检验项目配置ID")
    private Long itemConfigId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ApiModelProperty("标准规定")
    private String standardRule;

    @ApiModelProperty("是否可执行项")
    private Boolean isExecutable;

    @ApiModelProperty("是否报告项")
    private Boolean isReportable;

    @ApiModelProperty("执行方式（LIMS/ELN）")
    private ExecuteMethodEnum executeMethod;

    @ApiModelProperty("最终判定表达式")
    private String finalExpression;

    @ApiModelProperty("分析方法记录ID")
    private Long recordId;

    @ApiModelProperty("分析方法编码")
    private String recordCode;

    @ApiModelProperty("分析方法版本ID")
    private Long recordVersionId;

    @ApiModelProperty("分析方法记录项ID")
    private Long recordItemId;

    @ApiModelProperty("分析方法名称")
    private String recordName;

    @ApiModelProperty("分析方法版本")
    private String recordVersion;

    @ApiModelProperty("数据点配置列表")
    private List<StabilitySchemeDataPointRespVO> dataPoints;

    @ApiModelProperty("判定配置列表")
    private List<StabilitySchemeJudgmentRespVO> judgments;

    @ApiModelProperty("判定条件引用的数据点配置是否存在删除（true=存在问题）")
    private Boolean judgmentConfigError;

    @ApiModelProperty("判定引用的数据点是否存在删除（分析项聚合标识）")
    private Boolean judgmentDataPointDeleted;

    @ApiModelProperty("判定引用的数据点与记录组件绑定是否缺失（分析项聚合标识）")
    private Boolean judgmentDataPointBindingMissing;

    @ApiModelProperty("判定引用的数据点类型变更（分析项聚合标识）")
    private Boolean judgmentDataPointTypeChanged;

    @ApiModelProperty("判定引用的选项未在组件中配置（分析项聚合标识）")
    private Boolean judgmentDataPointOptionInvalid;
}
