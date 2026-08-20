package com.bmos.mes.service.execute.dto;

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
@ApiModel("完整合并数据查询DTO")
public class IntactMergeListQueryDTO {

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本号")
    @NotEmpty
    private String processVersion;

    @ApiModelProperty(value = "不归档的功能步骤", hidden = true)
    private String nodeFunction;
}
