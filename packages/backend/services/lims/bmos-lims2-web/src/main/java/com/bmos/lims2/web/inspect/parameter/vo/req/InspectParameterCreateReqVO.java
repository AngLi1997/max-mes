package com.bmos.lims2.web.inspect.parameter.vo.req;

import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterOptionDTO;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointTrendDTO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 保存分析项VO
 */
@Setter
@Getter
@ApiModel("保存分析项VO")
public class InspectParameterCreateReqVO {
    /**
     * 分析项名称
     */
    @ApiModelProperty(value = "分析项名称", required = true)
    @Length(max = 30)
    @NotBlank
    private String name;

    /**
     * 分析项编码
     */
    @ApiModelProperty(value = "分析项编码", required = true)
    @Length(max = 30)
    @NotBlank
    private String code;

    /**
     * 默认标准规定
     */
    @ApiModelProperty(value = "默认标准规定", required = false)
    private String standard;

    /**
     * 结果类型
     */
    @ApiModelEnumProperty(value = "结果类型", enumClass = AnalyzeResultTypeEnum.class, required = true)
    @NotNull
    private AnalyzeResultTypeEnum resultType;

    /**
     * 选项配置列表
     */
    @ApiModelProperty(value = "选项配置列表")
    private List<InspectParameterOptionDTO> options;

    /**
     * 趋势线配置列表
     */
    @ApiModelProperty(value = "趋势线配置列表")
    private List<InspectParameterDataPointTrendDTO> trends;
}
