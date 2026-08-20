package com.bmos.platform.facade.notify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("生产修订批量消息")
public class ProductModifyBatchMessage {

    @ApiModelProperty("消息列表")
    private List<ProductModifyAbnormalMessage> messageList;

    @ApiModelProperty("消息时间")
    private LocalDateTime time;

}
