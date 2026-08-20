package com.bmos.mes.service.formula.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("产品配方物料下拉列表VO")
public class ProductFormulaMaterialListVO {

    @ApiModelProperty("配方物料id")
    private Long id;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("删除数据标识")
    private Boolean disabled;


}
