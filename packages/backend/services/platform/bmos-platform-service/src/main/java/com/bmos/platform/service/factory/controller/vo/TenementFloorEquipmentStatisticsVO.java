package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className: TememtFloorEquipmentStatisticsVO
 * @author: yigaohui
 * @date: 2025/1/24 9:38
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("通过楼层id查询楼层绑定的设备统计信息")
public class TenementFloorEquipmentStatisticsVO {

    @ApiModelProperty("生产中")
    private int inProduction;

    @ApiModelProperty("可用")
    private int available;

    @ApiModelProperty("不可用")
    private int unavailable;

    @ApiModelProperty("故障")
    private int fault;
}
