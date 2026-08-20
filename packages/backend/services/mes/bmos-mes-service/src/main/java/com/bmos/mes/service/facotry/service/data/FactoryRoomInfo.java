package com.bmos.mes.service.facotry.service.data;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 房间清场信息
 */
@Getter
@Setter
public class FactoryRoomInfo extends BusinessDataHandleBaseDTO {

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房间编码
     */
    private String roomCode;

    /**
     * 产品编号
     */
    private String productNo;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 清场执行人id
     */
    private String operatorId;

    /**
     * 清场执行人名称
     */
    private String operator;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 清场日期
     */
    private String cleanDate;

    /**
     * 复核人id
     */
    private String verifierId;

    /**
     * 复核人姓名
     */
    private String verifier;

    /**
     * 复核时间
     */
    private String verifyDate;

    /**
     * 清场有效期
     */
    private String expireDate;

    /**
     * 清场有效期
     */
    private LocalDateTime expireTime;

}
