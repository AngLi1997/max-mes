package com.bmos.mes.service.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("生产物料编辑拓展信息DTO")
public class ProductMaterialUpdateExtensionDTO {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "生产周期(天)")
    private Integer productionCycle;

    @ApiModelProperty(value = "内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty(value = "包装规格")
    private String packingSpecification;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "制造商")
    private String manufacturer;

}
