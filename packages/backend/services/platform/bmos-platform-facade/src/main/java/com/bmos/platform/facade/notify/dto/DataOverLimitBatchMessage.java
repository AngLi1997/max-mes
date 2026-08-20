package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("数据超限批量消息")
public class DataOverLimitBatchMessage {

    @ApiModelProperty("时间")
    private LocalDateTime time;

    @ApiModelProperty("消息列表")
    private List<DataOverLimitMessage> messageList;


}
