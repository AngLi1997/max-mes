package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@ApiModel("领料计划VO")
@Getter
@Setter
public class RequisitionPlanVO {

    @ApiModelProperty("领料单名称")
    private String name;

    @ApiModelProperty("领料单id")
    private Long id;

    @ApiModelProperty("领料物料列表")
    private List<RequisitionPlanMaterialVO> materialList;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("是否完成向仓库发送领料计划")
    private Boolean completedPlan;

}
