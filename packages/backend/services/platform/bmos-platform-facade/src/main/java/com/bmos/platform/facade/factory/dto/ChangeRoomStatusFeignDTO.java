package com.bmos.platform.facade.factory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 状态变更FeignDTO
 */
@Getter
@Setter
public class ChangeRoomStatusFeignDTO {

    /**
     * 房间id 必传
     */
    @NotNull
    private Long id;

    /**
     * 房间状态 必传
     * {@link com.bmos.platform.facade.factory.enums.RoomStatusEnum}
     */
    @NotNull
    private Integer status;

    /**
     * 清洁结束时间 当房间为已清洁时必传
     */
    private LocalDateTime beginTime;

    /**
     * 清洁开始时间 当状态为已清洁时必传
     */
    private LocalDateTime endTime;

    /**
     * 有效时间 当状态为已清洁时必传
     */
    private LocalDateTime expireTime;

    /**
     * 生产批号 当房间状态变更为占用时必传
     */
    private String batchNo;

    /**
     * 产品名称 当房间状态变更为占用时必传
     */
    private String productName;

    /**
     * 工序id 当房间状态变更为占用时必传
     */
    private Long procedureId;

    /**
     * 工序名称 当房间状态变更为占用时必传
     */
    private String procedureName;

    /**
     * 操作人id
     */
    private String operateId;

    /**
     * 复核人id
     */
    private String verifyId;

    /**
     * 复核时间
     */
    private LocalDateTime verifyTime;

}
