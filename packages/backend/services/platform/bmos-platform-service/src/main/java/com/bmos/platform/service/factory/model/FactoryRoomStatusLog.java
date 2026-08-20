package com.bmos.platform.service.factory.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.io.Serializable;

/**
 * 房间状态变更日志(BpFactoryRoomStatusLog)实体类
 *
 * @author makejava
 * @since 2024-06-17 18:42:10
 */
@Getter
@Setter
@TableName("bp_factory_room_status_log")
public class FactoryRoomStatusLog extends BaseDO {
    /**
     * 主键id，模型的唯一标识
     */
    private Long id;
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
     * 变更前的状态
     */
    private Integer preStatus;
    /**
     * 变更状态
     */
    private Integer status;
    /**
     * 生产工序id
     */
    private Long procedureId;
    /**
     * 生产工序名称
     */
    private String procedureName;
    /**
     * 生产批号
     */
    private String batchNo;
    /**
     * 产品id
     */
    private Long productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 操作人id
     */
    private String operatorId;
    /**
     * 操作人姓名（账号名称-账号姓名）
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
     * 当为人工流转时的描述
     */
    private String description;

}

