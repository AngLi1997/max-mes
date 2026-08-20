package com.bmos.lims2.web.inspect.item.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 检验项下的分析项信息VO
 */
@Getter
@Setter
@ApiModel("检验项下的分析项信息的基础VO")
public class InspectItemParameterVO {

    @ApiModelProperty(value = "分析项id", required = true)
    @NotNull
    private Long inspectParameterId;

    @ApiModelProperty(value = "分析项编码")
    private String code;

    @ApiModelProperty(value = "分析项名称")
    private String name;

}
