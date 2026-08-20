package com.bmos.mes.service.requisition.dto;


import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("仓库领料:完成领料DTO")
@Data
public class RequisitionCompleteDTO extends BusinessDataHandleBaseDTO {

    @ApiModelProperty("领料单id")
    @NotNull
    private Long requisitionPlanId;


    @ApiModelProperty("计划人id")
    private String operatorId;
}
