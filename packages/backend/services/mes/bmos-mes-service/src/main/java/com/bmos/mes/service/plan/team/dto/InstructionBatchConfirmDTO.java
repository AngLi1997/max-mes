package com.bmos.mes.service.plan.team.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel("指令单批量确认列表DTO")
@Data
public class InstructionBatchConfirmDTO {

    @NotEmpty
    @ApiModelProperty("确认指令单列表")
    private List<InstructionConfirmDTO> instructionInfoList;

}
