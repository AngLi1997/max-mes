package com.bmos.lims2.server.audit.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel(value = "节点状态vo")
public class AuditNodeStateVO {

    @ApiModelProperty(value = "节点key")
    private String elementKey;

    @ApiModelProperty(value = "状态")
    private Integer state;
}
