package com.bmos.lims2.web.inspect.parameter.vo.resp;

import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分析项响应VO
 */
@Getter
@Setter
@ApiModel("分析项响应VO")
public class InspectParameterRespVO {

    /**
     * 分析项id
     */
    @ApiModelProperty(value = "分析项id")
    private Long id;

    /**
     * 分析项编码
     */
    @ApiModelProperty(value = "分析项编码")
    private String code;

    /**
     * 分析项名称
     */
    @ApiModelProperty(value = "分析项名称")
    private String name;

    /**
     * 当前分析项默认标准规定
     */
    @ApiModelProperty(value = "当前分析项默认标准规定")
    private String standard;

    /**
     * 选项配置列表
     */
    @ApiModelProperty(value = "选项配置列表")
    private List<InspectParameterOptionRespVO> options;

    /**
     * 趋势线配置列表
     */
    @ApiModelProperty(value = "趋势线配置列表")
    private List<InspectParameterTrendRespVO> trends;

    /**
     * 数据点列表
     */
    @ApiModelProperty(value = "数据点列表")
    private List<InspectParameterDataPointRespVO> dataPoints;
} 