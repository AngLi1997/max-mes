package com.bmos.lims2.server.inspect.parameter.dto;

import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分析项数据点DTO
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@ApiModel("分析项数据点DTO")
public class InspectParameterDataPointDTO {

    @ApiModelProperty("数据点id")
    private Long id;

    @ApiModelProperty("分析项id")
    private Long parameterId;

    @ApiModelProperty(value = "数据点名称", required = true)
    @NotBlank(message = "数据点名称不能为空")
    @Length(max = 64, message = "数据点名称长度不能超过64个字符")
    private String name;

    @ApiModelEnumProperty(value = "数据点结果类型", enumClass = AnalyzeResultTypeEnum.class, required = true)
    @NotNull(message = "数据点结果类型不能为空")
    private AnalyzeResultTypeEnum resultType;

    @ApiModelProperty("时间类型显示格式（仅当resultType为TIME时有效）")
    private String timeFormat;
    private String dateStyle;


    @ApiModelProperty("是否报告显示")
    private Boolean reportDisplay = true;

    @ApiModelProperty("选项配置列表")
    @Valid
    private List<InspectParameterOptionDTO> options;

    @ApiModelProperty("趋势线配置列表")
    @Valid
    private List<InspectParameterDataPointTrendDTO> trends;
} 