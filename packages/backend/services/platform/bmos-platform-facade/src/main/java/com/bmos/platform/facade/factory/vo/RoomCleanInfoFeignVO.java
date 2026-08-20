package com.bmos.platform.facade.factory.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 房间清场信息
 */
@Getter
@Setter
public class RoomCleanInfoFeignVO {

    /**
     * 房间id
     */
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
     * 过期时间
     */
    private LocalDateTime expireDate;

    /**
     * 当前房间最近一次的清场开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 当前房间最近一次的清场结束时间
     */
    private LocalDateTime endTime;

    /**
     * 当前房间最近一次的清场操作人id
     */
    private String operatorId;

    /**
     * 清场操作人姓名
     */
    private String operator;

    /**
     * 当前房间最近一次的清场复核人id
     */
    private String verifyId;

    /**
     * 复核人姓名
     */
    private String verifier;

    /**
     * 复核时间
     */
    private LocalDateTime verifyTime;

}
