package com.bmos.mes.service.requisition.vo;

import com.bmos.mes.common.enums.formula.DryAndPureTypeEnum;
import com.bmos.mes.common.enums.formula.QuantityTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequisitionPlanMaterialVO {

    @ApiModelProperty("配方物料id")
    private Long id;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("理论用量")
    private String theoreticalQuantity;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("折干折纯类型")
    private DryAndPureTypeEnum dryPureType;

    @ApiModelProperty("数量类型")
    private QuantityTypeEnum quantityType;

}
