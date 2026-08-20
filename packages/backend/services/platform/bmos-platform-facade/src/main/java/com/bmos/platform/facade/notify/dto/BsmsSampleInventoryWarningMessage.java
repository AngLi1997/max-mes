package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("血源样本库存预警消息")
public class BsmsSampleInventoryWarningMessage {

    private LocalDateTime time;

    @ApiModelProperty("新增预警数量")
    private Integer incrNum;

    @ApiModelProperty("总预警数量")
    private Integer allNum;
}
