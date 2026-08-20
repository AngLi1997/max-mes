package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @className: RoomListQueryVO
 * @author: yigaohui
 * @date: 2024/12/30 15:46
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("房间列表查询VO")
public class RoomListQueryVO {

    @ApiModelProperty("产线id集合")
    private List<Long> productLineIds;


    @ApiModelProperty("楼栋id集合")
    private List<Long> tenementIds;

    @ApiModelProperty("楼层id集合")
    private List<Long> tenementFloorIds;

    @ApiModelProperty("房间id集合")
    private List<Long> roomIds;

    @ApiModelProperty("洁净等级集合")
    private List<String> cleanLevels;
}
