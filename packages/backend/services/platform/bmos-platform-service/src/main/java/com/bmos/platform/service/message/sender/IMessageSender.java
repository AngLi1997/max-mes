package com.bmos.platform.service.message.sender;

import com.bmos.platform.service.message.dto.MessageContextDTO;

/**
 * @className: INotify
 * @author: yigaohui
 * @date: 2025/1/7 11:21
 * @Version: 1.0
 * @description:
 */


public interface IMessageSender {
    void send(MessageContextDTO message);
}
