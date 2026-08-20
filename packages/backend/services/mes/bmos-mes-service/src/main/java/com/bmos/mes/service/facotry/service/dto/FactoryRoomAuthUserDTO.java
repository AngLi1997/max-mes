package com.bmos.mes.service.facotry.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 房间清场人、QA人员VO
 */
@Getter
@Setter
@ApiModel("房间清场人、QA人员VO")
public class FactoryRoomAuthUserDTO {

    /**
     * 权限code
     */
    @ApiModelProperty(value = "权限code", required = true)
    private String authCode;

    /**
     * 房间id
     */
    @ApiModelProperty(value = "房间id", required = true)
    @NotNull
    private Long roomId;

}
