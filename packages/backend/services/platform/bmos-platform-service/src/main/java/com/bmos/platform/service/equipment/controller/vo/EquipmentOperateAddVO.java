package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 设备操作日志返回参数
 */
@Getter
@Setter
@ApiModel("设备操作日志添加参数")
public class EquipmentOperateAddVO {

    @ApiModelProperty("设备操作日志id,没有则不传")
    private Long id;

    /**
     * 设备id，关联到bp_equipment_info表中的id
     */
    @ApiModelProperty("设备id，关联到bp_equipment_info表中的id")
    private Long equipmentId;
    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    private String code;
    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;
    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    /**
     * 变更类型
     */
    @ApiModelProperty("变更类型")
    private String changeType;
    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 使用开始时间
     */
    @ApiModelProperty("使用开始时间")
    private LocalDateTime beginTime;

    /**
     * 使用结束时间
     */
    @ApiModelProperty("使用结束时间")
    private LocalDateTime endTime;
    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String operator;

    /**
     * 复核人
     */
    @ApiModelProperty("复核人")
    private String reviewer;

    @ApiModelProperty("操作内容")
    private String operateContent;
}

