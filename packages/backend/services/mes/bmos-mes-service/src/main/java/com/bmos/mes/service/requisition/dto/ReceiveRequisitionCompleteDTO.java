package com.bmos.mes.service.requisition.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("领料接收完成DTO")
@Data
public class ReceiveRequisitionCompleteDTO {

    @ApiModelProperty("领料单id")
    private Long requisitionPlanId;

}
