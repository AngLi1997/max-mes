package com.bmos.platform.service.factory.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("房间分页DTO")
public class RoomAppPageDTO extends BasePage {

    @NotNull
    @ApiModelProperty("产线id")
    private Long productionLineId;

}
