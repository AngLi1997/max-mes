package com.bmos.mes.service.preparation.measure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@ApiModel("配液量取添加物料件DTO")
@Data
public class LiquidPreparationAddMaterialDTO {

    @ApiModelProperty(value = "量取批次id", required = true)
    @NotNull
    private Long measureBatchId;

    /**
     * 消耗物料件id列表
     */
    @ApiModelProperty(value = "消耗物料件id列表")
    @NotNull
    private List<Long> consumeStorateMaterialIdList = new ArrayList<>();

}
