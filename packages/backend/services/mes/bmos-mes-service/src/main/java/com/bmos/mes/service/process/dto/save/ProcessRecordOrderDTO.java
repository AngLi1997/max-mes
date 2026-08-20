package com.bmos.mes.service.process.dto.save;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@ApiModel("记录项顺序DTO")
public class ProcessRecordOrderDTO {

    @ApiModelProperty("记录项id")
    @NotNull
    private Long recordItemId;

    @ApiModelProperty("记录项排序号")
    @NotNull
    private Long recordItemOrder;
}
