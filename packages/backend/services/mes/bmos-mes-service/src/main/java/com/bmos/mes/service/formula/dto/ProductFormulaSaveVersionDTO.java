package com.bmos.mes.service.formula.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("产品配方新增版本DTO")
public class ProductFormulaSaveVersionDTO {

    @ApiModelProperty("配方id")
    @NotNull
    private Long productFormulaId;

    @ApiModelProperty("配方版本")
    @NotEmpty
    private String versionNo;

    @ApiModelProperty("批量")
    @NotNull
    private BigDecimal batchQuantity;

    @ApiModelProperty("单位")
    @NotNull
    private Long unitId;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("物料列表")
    private List<ProductFormulaMaterialDTO> materialList;

}
