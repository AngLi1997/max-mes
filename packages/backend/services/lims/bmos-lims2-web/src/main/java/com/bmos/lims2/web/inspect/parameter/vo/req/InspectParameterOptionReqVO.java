package com.bmos.lims2.web.inspect.parameter.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 分析项数据点选项请求参数
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@ApiModel("分析项数据点选项请求参数")
public class InspectParameterOptionReqVO {

    @ApiModelProperty("选项id")
    private Long id;

    /**
     * 选项值
     */
    @ApiModelProperty(value = "选项值", required = true)
    @NotBlank(message = "选项值不能为空")
    private String optionValue;
} 