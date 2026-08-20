package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 产线更新DTO
 */
@Getter
@Setter
@ApiModel("产线更新DTO")
public class LineUpdateDTO extends LineSaveDTO{

    @ApiModelProperty("产线ID")
    @NotNull
    private Long id;

}
