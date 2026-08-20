package com.bmos.platform.service.equipment.controller.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel("设备操作日志返回参数")
public class EquipmentOperateLogVO {
    /**
     * id
     */
    @ApiModelProperty("设备id")
    private Long id;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;

    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    private String equipmentCode;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

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
     * 开始操作人
     */
    @ApiModelProperty("开始操作人")
    private String beginOperatorName;
    /**
     * 变更类型
     */
    @ApiModelProperty("变更类型")
    private String changeType;
    /**
     * 结束操作人
     */
    @ApiModelProperty("结束操作人")
    private String endOperatorName;


    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;


    /**
     * 复核人
     */
    @ApiModelProperty("复核人")
    private String reviewer;


    @ApiModelProperty("复核人名称")
    private String reviewerName;



    @ApiModelProperty("操作内容")
    private String operateContent;

    @ApiModelProperty("模板id")
    private Long templateId;
}

