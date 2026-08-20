package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 房间保存DTO
 */
@Getter
@Setter
@ApiModel("房间保存入参")
public class RoomUpdateDTO extends RoomSaveDTO {

    @ApiModelProperty(value = "房间id", required = true)
    @NotNull
    private Long id;

}
