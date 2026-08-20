package com.bmos.mes.service.preparation.produce.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 根据输入的物料编号查询物料批次信息
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("根据输入的物料编号查询物料批次信息")
public class PreparationMaterialBatchDTO {

    /**
     * 物料批次编号
     */
    @ApiModelProperty(value = "物料批次编号", required = true)
    @NotEmpty
    private String materialBatchNo;

    /**
     * 配方物料id
     */
    @ApiModelProperty(value = "配方物料id", required = true)
    @NotNull
    private Long formulaMaterialId;

}
