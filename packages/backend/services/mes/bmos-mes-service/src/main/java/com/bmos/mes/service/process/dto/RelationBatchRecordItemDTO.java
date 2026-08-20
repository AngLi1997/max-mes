package com.bmos.mes.service.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@ApiModel("工艺-关联批记录保存DTO")
public class RelationBatchRecordItemDTO {

    @ApiModelProperty(value = "批记录id",required = true)
    @NotNull
    private Long batchRecordId;

    @ApiModelProperty(value = "批记录版本id",required = true)
    @NotNull
    private Long batchRecordVersionId;

    @ApiModelProperty(value = "批记录版本号",required = true)
    @NotNull
    private String batchRecordVersion;

}
