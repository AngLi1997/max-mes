package com.bmos.mes.service.formula.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("产品配方版本详情VO")
public class ProductFormulaVersionDetailVO {
    @ApiModelProperty("配方版本id")
    private Long id;

    @ApiModelProperty("配方名称")
    private String name;

    @ApiModelProperty("配方版本")
    private String versionNo;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品合并编码")
    private String productMergeCode;

    @ApiModelProperty("批量")
    private BigDecimal batchQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("配方物料列表")
    private List<ProductFormulaMaterialVO> materialList;


}
