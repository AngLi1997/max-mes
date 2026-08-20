package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 产线绑定房间DTO
 */
@Getter
@Setter
@ApiModel("产线绑定房间DTO")
public class LineBindRoomDTO {

    @ApiModelProperty("产线ID")
    @NotNull
    private Long id;

    @ApiModelProperty("房间ID")
    @NotEmpty
    private List<Long> roomIdList;
}
