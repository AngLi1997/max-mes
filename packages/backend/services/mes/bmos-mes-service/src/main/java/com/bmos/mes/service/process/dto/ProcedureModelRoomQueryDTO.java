package com.bmos.mes.service.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("工序绑定房间查询DTO")
public class ProcedureModelRoomQueryDTO {

    @ApiModelProperty("工序模型id")
    @NotNull
    private Long procedureModelId;

    @ApiModelProperty("工步模型id")
    private Long stepModelId;


}
