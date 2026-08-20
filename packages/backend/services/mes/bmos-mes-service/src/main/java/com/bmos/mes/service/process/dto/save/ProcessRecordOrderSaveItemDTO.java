package com.bmos.mes.service.process.dto.save;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("工艺记录项顺序保存项DTO")
@Builder
public class ProcessRecordOrderSaveItemDTO {
    @Tolerate
    public ProcessRecordOrderSaveItemDTO(){}

    @ApiModelProperty("工序步骤模型id")
    private Long id;

    @ApiModelProperty("记录项id")
    @NotNull
    private Long recordItemId;

    @ApiModelProperty("记录项版本id")
    @NotNull
    private Long recordVersionId;

    @ApiModelProperty("记录项顺序")
    @NotNull
    private Long recordItemOrder;

    @ApiModelProperty("是否可复用")
    private Boolean reusable;

}
