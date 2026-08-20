package com.bmos.mes.service.preparation.measure.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("配液单详情查询DTO")
@Data
public class LiquidPreparationPlanDetailQueryDTO {


    @ApiModelProperty("配液单id")
    private Long liquidPreparationId;

    @ApiModelProperty("量取组件实例id")
    private Long  id;

}
