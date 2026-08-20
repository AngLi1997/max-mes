package com.bmos.lims2.web.inspect.parameter.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 分析项列表响应VO - 用于下拉选择
 * @author system
 */
@Getter
@Setter
@ApiModel("分析项列表响应VO")
public class InspectParameterListRespVO {

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
}