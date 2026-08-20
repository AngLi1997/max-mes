package com.bmos.lims2.server.material.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MaterialFieldQueryDTO {

    @ApiModelProperty("物料id")
    @NotNull
    private Long materialId;

    @ApiModelProperty("类型")
    private String fieldType;

}
