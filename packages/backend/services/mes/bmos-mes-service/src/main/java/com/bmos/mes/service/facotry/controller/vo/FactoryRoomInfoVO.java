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
public class FactoryRoomInfoVO {

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
     * 房间清场默认时限 单位为s
     */
    @ApiModelProperty(value = "房间清场默认时限 单位为s")
    private Long timeLimit;

}
