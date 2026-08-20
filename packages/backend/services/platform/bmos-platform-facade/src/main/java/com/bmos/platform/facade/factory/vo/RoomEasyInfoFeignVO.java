package com.bmos.platform.facade.factory.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomEasyInfoFeignVO {

    /**
     * 房间id
     */
    private Long id;

    /**
     * 房间编码
     */
    private String  code;

    /**
     * 房间名称
     */
    private String name;

}
