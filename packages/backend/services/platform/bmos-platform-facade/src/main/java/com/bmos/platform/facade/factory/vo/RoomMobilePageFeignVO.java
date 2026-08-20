package com.bmos.platform.facade.factory.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 移动端房间分页VO
 */
@Getter
@Setter
public class RoomMobilePageFeignVO {

    /**
     * 房间id
     */
    @ApiModelProperty("房间id")
    private Long id;

    /**
     * 房间编码
     */
    private String code;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 房间状态
     * {@link com.bmos.platform.facade.factory.enums.RoomStatusEnum}
     */
    private Integer status;

    /**
     * 房间清洁有效期
     */
    private String expireTime;

}
