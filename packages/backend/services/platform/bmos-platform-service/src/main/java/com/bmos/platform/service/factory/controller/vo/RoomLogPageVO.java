package com.bmos.platform.service.factory.controller.vo;

import com.bmos.platform.facade.factory.enums.RoomStatusOperateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 房间清场日志分页返回参数
 */
@Getter
@Setter
@ApiModel("房间清场日志分页返回参数")
public class RoomLogPageVO {

    /**
     * 日志id
     */
    @ApiModelProperty("日志id")
    private Long id;

    /**
     * 房间编码
     */
    @ApiModelProperty("房间编码")
    private String roomCode;

    /**
     * 房间名称
     */
    @ApiModelProperty("房间名称")
    private String roomName;

    /**
     * 清洁类型
     */
    @ApiModelProperty("清洁类型")
    private RoomStatusOperateTypeEnum type;

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
     *工序名称
     */
    @ApiModelProperty("工序名称")
    private String procedureName;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")
    private String expireTime;

    /**
     * 清场人
     */
    @ApiModelProperty("清场人")
    private String operator;

    /**
     * 复合人
     */
    @ApiModelProperty("复合人")
    private String verifier;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;
}
