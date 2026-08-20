package com.bmos.mes.service.preparation.measure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("配液量取结果查询DTO")
public class LiquidMeasureResultQueryDTO {

    @ApiModelProperty("量取组件实例id")
    private Long id;


}
