package com.bmos.mes.service.ingredient.plan.vo;

import com.bmos.mes.service.requisition.vo.RequisitionPlanMaterialVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("配料计划VO")
@Data
public class IngredientPlanVO {

    @ApiModelProperty("配料单名称")
    private String name;

    @ApiModelProperty("配料单id")
    private Long id;

    @ApiModelProperty("配料物料列表")
    private List<RequisitionPlanMaterialVO> materialList;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("配料计划是否完成")
    private Boolean completed;

}
