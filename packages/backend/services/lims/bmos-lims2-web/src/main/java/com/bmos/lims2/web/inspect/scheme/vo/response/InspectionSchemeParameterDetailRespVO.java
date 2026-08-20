package com.bmos.lims2.web.inspect.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验方案分析项配置详情响应VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案分析项配置详情响应")
public class InspectionSchemeParameterDetailRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("关联的方案ID")
    private Long schemeId;

    @ApiModelProperty("关联的版本ID")
    private Long versionId;

    @ApiModelProperty("实验包ID")
    private Long packageId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("关联的检验项目配置ID")
    private Long itemConfigId;

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ApiModelProperty("分析方法ID")
    private Long recordId;

    @ApiModelProperty("分析方法名称")
    private String recordName;

    @ApiModelProperty("标准规定")
    private String standardRule;

    @ApiModelProperty("是否报告项")
    private Boolean isReportable;

    @ApiModelProperty("是否可执行")
    private Boolean isExecutable;

    @ApiModelProperty("最终表达式")
    private String finalExpression;

    @ApiModelProperty("数据点配置列表")
    private List<InspectionSchemeDataPointRespVO> dataPoints;

    @ApiModelProperty("判定配置列表")
    private List<InspectionSchemeJudgmentRespVO> judgments;
}