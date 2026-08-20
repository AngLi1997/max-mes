package com.bmos.mes.service.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@ApiModel("公式查询DTO")
@Data
public class ExpressionQueryDTO {

    @NotEmpty
    @ApiModelProperty("组件类型")
    private String componentType;

    @ApiModelProperty("记录id")
    private Long recordId;

    @ApiModelProperty("组件公式id")
    private Long formulaId;
}
