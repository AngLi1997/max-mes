package com.bmos.platform.service.message.controller.vo;

import com.bmos.platform.service.message.constants.MessageTypeEnum;
import lombok.Data;

/**
 * @className: MessageCountDTO
 * @author: yigaohui
 * @date: 2025/1/8 16:54
 * @Version: 1.0
 * @description:
 */

@Data
public class MessageCountVO {
    private Integer count;

    private MessageTypeEnum messageType;
}
