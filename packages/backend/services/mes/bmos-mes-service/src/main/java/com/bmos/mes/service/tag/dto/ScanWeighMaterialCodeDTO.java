package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 13:51
 */
@Data
@ApiModel("扫描物料件号查询物料件信息参数")
public class ScanWeighMaterialCodeDTO {

    /**
     * 物料件号/设备编号
     */
    @ApiModelProperty(value = "物料件号/设备编号", example = "01", required = true)
    @NotBlank
    @Length(max = 100)
    private String no;

    /**
     * 目标单位id
     */
    @ApiModelProperty(value = "目标单位id", example = "1")
    private Long unitId;

    /**
     * 配料计划id
     */
    @ApiModelProperty(value = "配料计划id", example = "1")
    @NotNull
    private Long ingredientPlanId;

    /**
     * 物料批次id(校验)
     */
    @ApiModelProperty(value = "物料批次id(校验)", example = "1")
    @NotNull
    private Long materialBatchId;
}
