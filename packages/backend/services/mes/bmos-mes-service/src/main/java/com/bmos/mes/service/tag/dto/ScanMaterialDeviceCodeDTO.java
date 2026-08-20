package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 扫描物料件/设备号查询物料件信息参数
 * @author liang
 * @version 1.0.0
 * @date 2024/7/26 13:51
 */
@Data
@ApiModel("扫描物料件/设备号查询物料件信息参数")
public class ScanMaterialDeviceCodeDTO {

    /**
     * 物料件号/设备编号
     */
    @ApiModelProperty(value = "物料件号/设备编号", example = "01", required = true)
    @NotBlank
    @Length(max = 100)
    private String no;

    /**
     * 配方物料id(校验)
     */
    @ApiModelProperty(value = "配方物料id(校验)", example = "1")
    private Long formulaMaterialId;

    /**
     * 查询结果转换目标单位id
     */
    @ApiModelProperty(value = "查询结果转换目标单位id", example = "1")
    private Long unitId;

    /**
     * 生产计划id(校验)
     */
    @ApiModelProperty(value = "生产计划id(校验是否预定到了该批次)", example = "1")
    private Long productPlanId;

    /**
     * 是否已出库(校验)
     */
    @ApiModelProperty(value = "是否已出库(校验)", example = "true")
    private Boolean isOutbound;

    /**
     * 是否可用(校验)
     */
    @ApiModelProperty(value = "是否可用(校验)", example = "true")
    private Boolean isAvailable;
}
