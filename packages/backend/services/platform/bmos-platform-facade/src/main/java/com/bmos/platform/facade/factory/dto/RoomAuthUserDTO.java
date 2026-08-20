package com.bmos.platform.facade.factory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 获取具有权限的清场人/QA人员DTO
 */
@Getter
@Setter
@ApiModel("获取具有权限的清场人/QA人员DTO")
public class RoomAuthUserDTO {

    /**
     * 房间id
     */
    @ApiModelProperty(value = "房间id", required = true)
    @NotNull
    private Long roomId;

    /**
     * 权限码
     */
    @ApiModelProperty(value = "权限码", required = true)
    @NotEmpty
    private String authCode;

}
