package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 确认配液投入时扫描物料件/容器查询物料件信息
 */
@Data
@ApiModel("确认配液投入时扫描物料件/容器查询物料件信息DTO")
public class ScanPreparationMaterialCodeDTO {

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
    private Long preparationPlanId;

    /**
     * 物料批次id(校验)
     */
    @ApiModelProperty(value = "物料批次id(校验)", example = "1")
    @NotNull
    private Long materialBatchId;
}
