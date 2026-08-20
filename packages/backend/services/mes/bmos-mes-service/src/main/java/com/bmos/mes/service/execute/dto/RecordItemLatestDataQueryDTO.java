package com.bmos.mes.service.execute.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("查询记录项最新值DTO")
public class RecordItemLatestDataQueryDTO {

    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long productPlanId;

    @ApiModelProperty(value = "工序步骤id",required = true)
    @NotNull
    private Long procedureStepId;

    @ApiModelProperty("记录项id")
    @NotNull
    private Long recordItemId;

    @ApiModelProperty(value = "复制版本号，未被复制传0",required = true)
    @NotNull
    private Long copyVersion;

    @ApiModelProperty("是否查询废弃值")
    private Boolean discard;

    @ApiModelProperty(value = "是否复用",required = true)
    @NotNull
    private Boolean reuse;

    @ApiModelProperty(hidden = true)
    private List<Long> fieldIdList;
}
