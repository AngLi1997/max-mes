package com.bmos.mes.service.facotry.controller.vo;

import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 移动端房间分页VO
 */
@Getter
@Setter
@ApiModel("移动端房间分页VO")
public class RoomMobilePageVO {

    /**
     * 房间id
     */
    @ApiModelProperty("房间id")
    private Long id;

    /**
     * 房间编码
     */
    @ApiModelProperty("房间编码")
    private String code;

    /**
     * 房间名称
     */
    @ApiModelProperty("房间名称")
    private String name;

    /**
     * 房间状态
     */
    @ApiModelProperty("房间状态")
    private RoomStatusEnum status;

    /**
     * 房间清洁有效期
     */
    @ApiModelProperty("房间清洁有效期")
    private String expireTime;

}
