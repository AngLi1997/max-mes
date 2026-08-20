package com.bmos.mes.service.facotry.service.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("清场相关组件扫描二维码时获取房间详情")
public class CleanExecuteRoomInfoDTO extends BusinessDataHandleBaseDTO {

    @ApiModelProperty("房间id")
    private Long roomId;
}
