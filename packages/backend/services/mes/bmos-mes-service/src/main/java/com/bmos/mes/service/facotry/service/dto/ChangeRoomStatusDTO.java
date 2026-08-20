package com.bmos.mes.service.facotry.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 房间状态变更入参
 */
@Getter
@Setter
@ApiModel("房间状态变更入参")
public class ChangeRoomStatusDTO {

    /**
     * 房间id
     */
    @ApiModelProperty(value = "房间id", required = true)
    @NotNull
    private Long id;

    /**
     * 房间状态
     */
    @ApiModelProperty(value = "房间状态", required = true)
    @NotNull
    private Integer status;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", notes = "当房间状态变更为占用/已清洁时必传")
    private String batchNo;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id", notes = "当房间状态变更为占用/已清洁时必传")
    private Long productId;

    /**
     * 工序id
     */
    @ApiModelProperty(value = "工序id", notes = "当房间状态变更为占用/已清洁时必传")
    private Long procedureId;

    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称", notes = "当房间状态变更为占用/已清洁时必传")
    private String procedureName;

    /**
     * 清场开始时间
     */
    @ApiModelProperty(value = "清场开始时间", notes = "当房间状态变更为已清洁时必传")
    private String beginTime;

    /**
     * 清场结束时间
     */
    @ApiModelProperty(value = "清场结束时间", notes = "当房间状态变更为已清洁时必传")
    private String endTime;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间", notes = "当房间状态变更为占用/已清洁时必传")
    private String expireTime;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", required = true)
    private String desc;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", required = true)
    private String verifyId;

    /**
     * 状态变更操作人id
     */
    @ApiModelProperty(value = "状态变更操作人id", required = true)
    private String operateId;
}
