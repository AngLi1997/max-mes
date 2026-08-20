package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("暂存物料单个取消预定DTO")
public class StorageMaterialCancelReservedSingleDto {

    /**
     * 暂存物料id
     */
    @ApiModelProperty(value = "暂存物料id", example = "1", required = true)
    @NotNull
    private Long storageMaterialId;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id", example = "1", required = true)
    @NotNull
    private Long processId;

    /**
     * 生产批次id
     */
    @ApiModelProperty(value = "生产批次id", example = "1", required = true)
    @NotNull
    private Long batchId;

}
