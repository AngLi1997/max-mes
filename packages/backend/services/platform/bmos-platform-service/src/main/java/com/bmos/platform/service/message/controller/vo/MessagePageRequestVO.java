package com.bmos.platform.service.message.controller.vo;

import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.service.message.constants.MessageStatusEnum;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @className: MessagePageRequestVO
 * @author: yigaohui
 * @date: 2025/1/8 17:39
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("消息分页查询VO")
public class MessagePageRequestVO extends BasePage {
    private List<MessageTypeEnum> messageType;

    private MessageStatusEnum messageStatus;
}
