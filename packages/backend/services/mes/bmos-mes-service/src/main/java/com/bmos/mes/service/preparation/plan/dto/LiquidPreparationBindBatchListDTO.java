package com.bmos.mes.service.preparation.plan.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("配液计划绑定批次列表DTO")
@Data
public class LiquidPreparationBindBatchListDTO {


    @ApiModelProperty("物料批次id")
    @NotNull(message = "物料批次id不能为空")
    private Long materialBatchId;

    @ApiModelProperty("物料批次编码")
    @NotEmpty(message = "物料批次编码不能为空")
    private String materialBatchNo;

    @ApiModelProperty("配料量")
    @NotNull(message = "配液量不能为空")
    private BigDecimal preparationQuantity;
}
