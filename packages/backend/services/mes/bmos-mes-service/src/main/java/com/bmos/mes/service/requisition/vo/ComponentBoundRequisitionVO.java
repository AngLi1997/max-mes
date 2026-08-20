package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("组件绑定领料单信息VO")
@Data
public class ComponentBoundRequisitionVO {

    @ApiModelProperty("领料单id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("是否已完成接收")
    private Boolean completedReceive;

}
