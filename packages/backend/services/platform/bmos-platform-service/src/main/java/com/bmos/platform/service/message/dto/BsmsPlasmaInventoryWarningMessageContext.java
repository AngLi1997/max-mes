package com.bmos.platform.service.message.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("血源血浆库存预警消息")
public class BsmsPlasmaInventoryWarningMessageContext extends MessageContextDTO {

    private LocalDateTime time;

    @ApiModelProperty("新增预警数量")
    private Integer incrNum;

    @ApiModelProperty("总预警数量")
    private Integer allNum;
}
