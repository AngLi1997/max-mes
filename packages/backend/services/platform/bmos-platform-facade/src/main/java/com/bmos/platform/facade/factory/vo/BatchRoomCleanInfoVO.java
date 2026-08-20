package com.bmos.platform.facade.factory.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BatchRoomCleanInfoVO {

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房间编码
     */
    private String roomCode;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 清场开始时间
     */
    private LocalDateTime cleanStartTime;

    /**
     * 清场结束时间
     */
    private LocalDateTime cleanEndTime;

    /**
     * 清场有效期至
     */
    private LocalDateTime validTime;

    /**
     * 清场人
     */
    private String operator;

    /**
     * 复核时间
     */
    private LocalDateTime verifyTime;

    /**
     * 复核人
     */
    private String verifier;

    /**
     * 操作时间
     */
    private LocalDateTime operateTime;

    /**
     * 描述
     */
    private String desc;
}
