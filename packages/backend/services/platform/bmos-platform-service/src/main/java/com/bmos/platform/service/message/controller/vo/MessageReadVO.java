package com.bmos.platform.service.message.controller.vo;

import com.bmos.platform.service.message.constants.MessageTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 消息已读请求接口
 *
 * @className: MessageReadVO
 * @author: yigaohui
 * @date: 2025/1/14 9:39
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("消息已读")
public class MessageReadVO {

    @ApiModelProperty("消息id集合")
    private List<Long> ids;

    @ApiModelProperty("是否全部已读")
    private boolean all;

    @ApiModelProperty("消息类型")
    private List<MessageTypeEnum> messageType;
}
