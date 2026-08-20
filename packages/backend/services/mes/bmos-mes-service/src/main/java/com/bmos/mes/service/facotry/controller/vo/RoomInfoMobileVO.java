package com.bmos.mes.service.facotry.controller.vo;

import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 移动端获取房间信息VO
 */
@Getter
@Setter
@ApiModel("房间信息VO")
public class RoomInfoMobileVO {

    /**
     * 房间ID
     */
    @ApiModelProperty("房间ID")
    private Long id;

    /**
     * 房间编号
     */
    @ApiModelProperty("房间编号")
    private String code;

    /**
     * 房间名称
     */
    @ApiModelProperty("房间名称")
    private String name;

    /**
     * 房间状态
     */
    @ApiModelProperty("房间状态")
    private RoomStatusEnum status;

    /**
     * 默认清洁有效期
     */
    @ApiModelProperty("时间限制")
    private Long timeLimit;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    /**
     * 工序名称
     */
    @ApiModelProperty("工序名称")
    private String procedureName;

    /**
     * 过期时间
     */
    @ApiModelProperty("过期时间")
    private LocalDateTime expireTime;

}
