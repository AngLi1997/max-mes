package com.bmos.mes.service.facotry.service.data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 查询生产计划所使用的工艺中工序内组件上所绑定的房间信息(剔除不属于生产计划中所配置的产线id)
 */
@Getter
@Setter
@ApiModel("查询生产计划所使用的工艺中工序内组件上所绑定的房间信息(剔除不属于生产计划中所配置的产线id)入参")
public class PlanComponentRoomDTO {

    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private Long planId;

    /**
     * 工序步骤模型id
     */
    @ApiModelProperty("工序步骤模型id")
    private Long procedureStepModelId;

    /**
     * 组件id
     */
    @ApiModelProperty("组件id")
    private Long componentId;

}
