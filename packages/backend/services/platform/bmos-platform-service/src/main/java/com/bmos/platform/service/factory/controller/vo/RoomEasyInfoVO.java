package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("房间简易信息")
public class RoomEasyInfoVO {
    /**
     * 房间id
     */
    @ApiModelProperty("房间id")
    private Long id;

    /**
     * 房间编码
     */
    @ApiModelProperty("房间编码")
    private String  code;

    /**
     * 房间名称
     */
    @ApiModelProperty("房间名称")
    private String name;
}
