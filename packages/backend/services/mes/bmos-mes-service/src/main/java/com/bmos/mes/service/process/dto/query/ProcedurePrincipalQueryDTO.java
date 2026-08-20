package com.bmos.mes.service.process.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("工序节点负责人查询DTO")
public class ProcedurePrincipalQueryDTO {

    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "工艺版本",required = true)
    @NotEmpty
    private String processVersion;

    @ApiModelProperty(value = "节点id",required = true)
    @NotEmpty
    private String nodeId;

}
