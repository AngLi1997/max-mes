package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("领料计划:物料件批量预定DTO")
@Data
public class StorageMaterialReserveBatchDTO {

    /**
     * 暂存物料id
     */
    @ApiModelProperty(value = "暂存物料id列表", example = "1", required = true)
    @NotEmpty
    private List<Long> storageMaterialIdList;

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

    @ApiModelProperty("物料id")
    private Long materialId;


}
