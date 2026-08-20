package com.bmos.mes.service.facotry.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 房间vo
 */
@Getter
@Setter
@ApiModel(value = "房间vo")
public class FactoryRoomCleanInfoVO {

    /**
     * 房间id
     */
    @ApiModelProperty(value = "房间id")
    private Long id;

    /**
     * 房间名称
     */
    @ApiModelProperty(value = "房间名称")
    private String name;

    /**
     * 房间code
     */
    @ApiModelProperty(value = "房间code")
    private String code;

    /**
     * 房间状态
     * {@link com.bmos.platform.facade.factory.enums.RoomStatusEnum}
     */
    @ApiModelProperty(value = "房间状态")
    private Integer status;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expireDate;

    /**
     * 当前房间最近一次的清场开始时间
     */
    @ApiModelProperty(value = "当前房间最近一次的清场开始时间")
    private LocalDateTime beginTime;

    /**
     * 当前房间最近一次的清场结束时间
     */
    @ApiModelProperty(value = "当前房间最近一次的清场结束时间")
    private LocalDateTime endTime;

    /**
     * 当前房间最近一次的清场操作人姓名
     */
    @ApiModelProperty(value = "当前房间最近一次的清场操作人姓名")
    private String operator;

    /**
     * 当前房间最近一次的清场复核人姓名
     */
    @ApiModelProperty(value = "当前房间最近一次的清场复核人姓名")
    private String verifier;

    /**
     * 复核时间
     */
    @ApiModelProperty(value = "复核时间")
    private LocalDateTime verifyTime;

}
