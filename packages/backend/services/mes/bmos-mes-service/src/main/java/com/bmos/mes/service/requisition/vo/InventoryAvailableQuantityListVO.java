package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("仓库可用量列表VO")
@Data
public class InventoryAvailableQuantityListVO {
    @ApiModelProperty("物料id")
    private Long id;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String mergeCode;

    @ApiModelProperty("物料规格")
    private String specification;

    @ApiModelProperty("库存量")
    private BigDecimal inventoryQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty(value = "供应商", example = "供应商")
    private String supplier;

    @ApiModelProperty(value = "生产商", example = "生产商")
    private String producer;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("领料量")
    private BigDecimal reservedQuantity;

    @ApiModelProperty("是否已预订")
    private Boolean reserved;

}
