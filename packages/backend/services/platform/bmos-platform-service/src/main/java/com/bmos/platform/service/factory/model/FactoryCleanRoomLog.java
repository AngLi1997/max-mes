package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 房间清场日志(BpFactoryRoomLog)实体类
 *
 * @author makejava
 * @since 2024-05-21 10:16:25
 */
@Getter
@Setter
@TableName("bp_factory_room_clean_log")
public class FactoryCleanRoomLog extends BaseDO {

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
     * 清场类型
     * {@link com.bmos.platform.facade.factory.enums.RoomStatusOperateTypeEnum}
     */
    private String type;
    /**
     * 生产批号
     */
    private String batchNo;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 生产工序id
     */
    private Long procedureId;
    /**
     * 生产工序名称
     */
    private String procedureName;
    /**
     * 开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 清场有效期
     */
    private LocalDateTime expireTime;
    /**
     * 清场操作人id
     */
    private String operatorId;
    /**
     * 清场操作人姓名（账号名称-账号姓名）
     */
    private String operator;
    /**
     * 复核人id
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
    /**
     * 当为人工清场时的清场描述
     */
    private String description;

}

