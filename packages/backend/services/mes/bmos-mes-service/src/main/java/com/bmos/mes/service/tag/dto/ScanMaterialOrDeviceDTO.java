package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class ScanMaterialOrDeviceDTO {

    @ApiModelProperty("物料件号或设备编码")
    @NotBlank
    private String code;

    @ApiModelProperty("投料回收主键id(非组件id)")
    @NotNull
    private Long chargeRecycleId;


}
