package com.bmos.platform.service.equipment.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取工位下的设备信息
 */
@Getter
@Setter
@ApiModel("获取工位下的设备信息入参")
public class EquipmentStationPageDTO extends BasePage {

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("工位id列表")
    private String stationIdStr;

    @ApiModelProperty(hidden = true)
    private List<Long> stationIdList = new ArrayList<>();

}
