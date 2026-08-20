package com.bmos.platform.service.equipment.model;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.common.enums.equipment.OperateLogFillingStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * 设备操作日志表，记录设备的操作日志信息(BpEquipmentOperateLog)实体类
 *
 * @author makejava
 * @since 2024-04-22 20:39:05
 */
@Getter
@Setter
@TableName("bp_equipment_operate_log")
public class EquipmentOperateLog extends BaseDO implements Serializable {

    /**
     * 设备id，关联到bp_equipment_info表中的id
     */
    private Long equipmentId;
    /**
     * 设备编码
     */
    private String equipmentCode;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 生产批号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String batchNo;
    /**
     * 变更类型
     */
    private String changeType;
    /**
     * 产品名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String productName;
    /**
     * 使用开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 使用结束时间
     */
    private LocalDateTime endTime;
    /**
     * 开始操作人id
     */
    private String beginOperator;

    /**
     * 开始操作人姓名
     */
    private String beginOperatorName;
    /**
     * 结束操作人id
     */
    private String endOperator;

    /**
     * 结束操作人姓名
     */
    private String endOperatorName;

    /**
     * 复核人
     */
    private String reviewer;


    private String reviewerName;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String operateContent;

    /**
     * 模板id 用于前端回显
     */
    private Long templateId;

    /**
     * 填报状态
     */
    private OperateLogFillingStatusEnum fillStatus;
}

