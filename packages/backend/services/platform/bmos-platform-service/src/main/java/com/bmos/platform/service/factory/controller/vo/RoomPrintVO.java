package com.bmos.platform.service.factory.controller.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 房间打印VO
 */
@Getter
@Setter
public class RoomPrintVO {

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 房间编码
     */
    private String roomCode;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 清场时限
     */
    private String timeLimit;
}
