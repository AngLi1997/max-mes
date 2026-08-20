package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("扫描投料回收设备")
@Data
public class ScanChargeRecycleDeviceCodeDTO {

    @ApiModelProperty(value = "设备编号", example = "01", required = true)
    @NotBlank
    @Length(max = 100)
    private String deviceCode;

    @ApiModelProperty("投料回收主键id(非组件id)")
    @NotNull
    private Long chargeRecycleId;

}
