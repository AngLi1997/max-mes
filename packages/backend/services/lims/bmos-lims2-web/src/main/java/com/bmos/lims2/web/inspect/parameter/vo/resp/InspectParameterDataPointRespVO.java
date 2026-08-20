package com.bmos.lims2.web.inspect.parameter.vo.resp;

import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分析项数据点响应参数
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@ApiModel("分析项数据点响应参数")
public class InspectParameterDataPointRespVO {

    @ApiModelProperty("数据点id")
    private Long id;

    @ApiModelProperty("数据点名称")
    private String name;

    @ApiModelProperty("数据点类型")
    @ApiModelEnumProperty(value = "数据点类型", enumClass =AnalyzeResultTypeEnum.class)
    private AnalyzeResultTypeEnum resultType;

    @ApiModelProperty("时间类型显示格式（仅当resultType为TIME时有效）")
    private String timeFormat;

    private String dateStyle;


    @ApiModelProperty("标准规定")
    private String standard;

    @ApiModelProperty("是否报告显示")
    private Boolean reportDisplay;

    @ApiModelProperty("选项列表")
    private List<InspectParameterOptionRespVO> options;

    @ApiModelProperty("趋势线列表")
    private List<InspectParameterTrendRespVO> trends;
} 