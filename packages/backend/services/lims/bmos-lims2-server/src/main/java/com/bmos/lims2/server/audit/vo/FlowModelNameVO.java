package com.bmos.lims2.server.audit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "流程模型名称返回vo")
public class FlowModelNameVO {

    @ApiModelProperty(value = "流程模型名称")
    private String name;

    @ApiModelProperty(value = "流程模型id")
    private Long id;
}
