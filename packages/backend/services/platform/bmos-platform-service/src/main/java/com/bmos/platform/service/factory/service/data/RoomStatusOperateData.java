package com.bmos.platform.service.factory.service.data;

import com.bmos.platform.facade.factory.enums.RoomStatusOperateTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 房间状态操作数据
 */
@Getter
@Setter
public class RoomStatusOperateData {

    /**
     * 房间id
     */
    private Long id;

    /**
     * 房间状态
     */
    private Integer status;

    /**
     * 生产批号 当房间状态变更为占用时必传
     */
    private String batchNo;

    /**
     * 产品id 当房间状态变更为占用时必传
     */
    private Long productId;

    /**
     * 产品名称 当房间状态变更为占用时必传
     */
    private String productName;

    /**
     * 工序id 当房间状态变更为占用时必传
     */
    private Long procedureId;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 描述
     */
    private String desc;

    /**
     * 操作人id
     */
    private String operateId;

    /**
     * 复核人id
     */
    private String verifierId;

    /**
     * 清场开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 清场结束时间
     */
    private LocalDateTime endTime;

    /**
     * 有效期
     */
    private LocalDateTime expireTime;

    /**
     * 操作类型
     */
    private RoomStatusOperateTypeEnum operateTypeEnum;

    /**
     * 复核时间
     */
    private LocalDateTime verifyTime;

}
