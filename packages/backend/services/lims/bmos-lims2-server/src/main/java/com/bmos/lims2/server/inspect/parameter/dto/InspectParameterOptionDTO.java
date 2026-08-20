package com.bmos.lims2.server.inspect.parameter.dto;

import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分析项数据点选项DTO
 */
@Getter
@Setter
@ApiModel("分析项数据点选项DTO")
public class InspectParameterOptionDTO extends BaseDO {

    /**
     * 分析项id
     */
    @ApiModelProperty(value = "分析项id", required = true)
    private Long parameterId;

    /**
     * 选项值
     */
    @ApiModelProperty(value = "选项值", required = true)
    private String optionValue;
} 