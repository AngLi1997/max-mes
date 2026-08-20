package com.bmos.platform.service.factory.controller.vo;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("房间分页VO")
public class RoomAppPageVO {

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

    public RoomStatusEnum getStatus() {
        return CommonEnum.getEnumByValue(RoomStatusEnum.class, status);
    }

}
