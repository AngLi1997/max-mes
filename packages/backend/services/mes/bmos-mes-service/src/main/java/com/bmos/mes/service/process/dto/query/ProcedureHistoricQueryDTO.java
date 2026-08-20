package com.bmos.mes.service.process.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("历史工序查询DTO")
public class ProcedureHistoricQueryDTO {

    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty("名称")
    private String name;
}
