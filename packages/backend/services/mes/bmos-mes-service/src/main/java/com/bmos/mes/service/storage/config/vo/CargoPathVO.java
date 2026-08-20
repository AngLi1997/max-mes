package com.bmos.mes.service.storage.config.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CargoPathVO {

    @ApiModelProperty("货位id")
    private Long id;

    @ApiModelProperty("货位路径")
    private String cargoPath;

}
