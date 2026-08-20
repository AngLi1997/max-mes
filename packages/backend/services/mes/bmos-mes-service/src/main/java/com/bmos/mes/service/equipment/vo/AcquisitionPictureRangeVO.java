package com.bmos.mes.service.equipment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("设备数采绘图区间VO")
@Data
public class AcquisitionPictureRangeVO {

    @ApiModelProperty("纵轴上限")
    private String upperValue;

    @ApiModelProperty("纵轴下限")
    private String lowerValue;


}
