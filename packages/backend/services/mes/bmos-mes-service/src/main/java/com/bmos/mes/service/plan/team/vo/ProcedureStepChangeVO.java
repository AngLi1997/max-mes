package com.bmos.mes.service.plan.team.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("工步换班列表VO")
@Data
public class ProcedureStepChangeVO {

    @ApiModelProperty("工步id")
    private Long procedureStepId;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    public String getChangeNumberStr() {
        return processChangeNumber + "-" + procedureChangeNumber;
    }

}
