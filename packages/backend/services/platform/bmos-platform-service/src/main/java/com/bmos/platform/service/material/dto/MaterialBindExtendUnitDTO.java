package com.bmos.platform.service.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("物料绑定拓展单位DTO")
public class MaterialBindExtendUnitDTO {

    @ApiModelProperty("物料id")
    @NotNull
    private Long materialId;

    @ApiModelProperty("拓展单位ids")
    private List<Long> extendUnitIdList;

}
