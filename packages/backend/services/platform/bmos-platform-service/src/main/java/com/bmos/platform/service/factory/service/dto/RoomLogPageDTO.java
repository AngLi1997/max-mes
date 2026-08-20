package com.bmos.platform.service.factory.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 房间清场日志分页入参
 */
@Getter
@Setter
@ApiModel("房间清场日志分页入参")
public class RoomLogPageDTO extends BasePage {

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
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    private String endTime;

}
