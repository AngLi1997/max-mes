package com.bmos.mes.service.preparation.measure.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel("配液单详情VO")
@Data
public class LiquidPreparationDetailVO {

    @ApiModelProperty(value = "配液单id", example = "1")
    private Long id;

    @ApiModelProperty(value = "配液单名称", example = "人血白蛋白-2402016-01")
    private String name;

    @ApiModelProperty("配液批次列表")
    private List<LiquidPreparationDetailBatchVO> batchList = new ArrayList<>();


}
