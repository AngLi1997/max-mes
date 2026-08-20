package com.bmos.lims2.web.inspect.parameter.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分析项数据点选项响应参数
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
@ApiModel("分析项数据点选项响应参数")
public class InspectParameterOptionRespVO {

    /**
     * 选项id
     */
    @ApiModelProperty("选项id")
    private Long id;

    /**
     * 选项值
     */
    @ApiModelProperty("选项值")
    private String optionValue;
} 