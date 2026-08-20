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
public class UserBindStationsDTO {

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    @NotNull
    private String userId;

    @ApiModelProperty("工位id集合")
    @NotEmpty
    private List<Long> stationIdList;

    /**
     * 前端页面显示的所有的工位
     */
    @ApiModelProperty("前端页面显示的所有的工位")
    private List<Long> allStationIdList;

}
