package com.bmos.mes.service.preparation.measure.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("配液量取物料件扫描DTO")
@Data
public class LiquidMeasureMaterialPieceQueryDTO {

    @ApiModelProperty("物料件号或容器编号")
    @NotNull
    private String code;

    @ApiModelProperty("物料批次id")
    @NotNull
    private Long materialBatchId;

    @ApiModelProperty("配液量取组件实例id")
    @NotNull
    private Long id;

}
