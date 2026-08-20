package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 工位绑定用户入参
 */
@Getter
@Setter
@ApiModel("工位绑定用户入参")
public class StationBindUserDTO {

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    @NotNull
    private Long stationId;

    @ApiModelProperty("用户id")
    private List<String> userIdList;

}
