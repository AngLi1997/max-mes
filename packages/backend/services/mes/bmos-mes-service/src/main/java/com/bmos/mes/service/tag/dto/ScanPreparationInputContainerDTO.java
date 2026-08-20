package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 配液投入容器扫描DTO
 */
@Getter
@Setter
@ApiModel("配液投入容器扫描DTO")
public class ScanPreparationInputContainerDTO extends ScanPreparationInputDTO {

    /**
     * 设备编码
     */
    @ApiModelProperty(value = "设备编码",required = true)
    @NotEmpty
    private String code;

}
