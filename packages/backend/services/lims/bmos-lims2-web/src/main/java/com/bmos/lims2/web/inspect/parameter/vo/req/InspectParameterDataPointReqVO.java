package com.bmos.lims2.web.inspect.parameter.vo.req;

import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分析项数据点请求参数
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@ApiModel("分析项数据点请求参数")
public class InspectParameterDataPointReqVO {

    @ApiModelProperty(value = "数据点名称", required = true)
    @NotBlank(message = "数据点名称不能为空")
    private String name;

    @ApiModelProperty(value = "数据点类型", required = true)
    @NotNull(message = "数据点类型不能为空")
    @ApiModelEnumProperty(value = "数据点类型" ,enumClass = AnalyzeResultTypeEnum.class)
    private AnalyzeResultTypeEnum resultType;

    @ApiModelProperty("时间类型显示格式（仅当resultType为TIME时有效）")
    private String timeFormat;

    private String dateStyle;


    @ApiModelProperty(value = "是否报告显示", required = true)
    @NotNull(message = "是否报告显示不能为空")
    private Boolean reportDisplay;

    @ApiModelProperty("选项列表")
    @Valid
    private List<InspectParameterOptionReqVO> options;

    @ApiModelProperty("趋势线列表")
    @Valid
    private List<InspectParameterTrendReqVO> trends;
} 