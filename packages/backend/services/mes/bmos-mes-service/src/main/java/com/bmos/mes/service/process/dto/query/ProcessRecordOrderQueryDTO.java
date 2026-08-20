package com.bmos.mes.service.process.dto.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("记录项顺序查询DTO")
public class ProcessRecordOrderQueryDTO {

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本")
    @NotEmpty
    private String processVersion;

    @ApiModelProperty(value = "排除的功能节点",hidden = true)
    private String nodeFunction;
}
