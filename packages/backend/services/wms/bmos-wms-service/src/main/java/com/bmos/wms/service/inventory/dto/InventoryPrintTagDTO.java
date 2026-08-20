package com.bmos.wms.service.inventory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("物料件标签打印请求")
public class InventoryPrintTagDTO {

    @ApiModelProperty(value = "货品件id", required = true, example = "1")
    @NotNull(message = "货品件id不能为空")
    private Long id;
}
