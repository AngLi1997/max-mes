package com.bmos.mes.service.preparation.plan.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("配液量列表批量计算")
@Data
public class LiquidPreparationQuantityCalculateVO {

    @ApiModelProperty
    private List<LiquidPreparationQuantityVO> preparationQuantityList;

    @ApiModelProperty("是否已经满足")
    private Boolean satisfied;


}
