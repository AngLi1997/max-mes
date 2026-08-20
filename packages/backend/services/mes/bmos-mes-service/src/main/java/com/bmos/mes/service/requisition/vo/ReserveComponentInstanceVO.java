package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("物料预定组件实例VO")
@Data
public class ReserveComponentInstanceVO {

    @ApiModelProperty("组件实例id")
    private Long componentInstanceId;

    @ApiModelProperty("领料物料列表")
    private List<RequisitionPlanMaterialVO> materialList;

}
