package com.bmos.platform.service.message.service;

import com.bmos.platform.service.message.constants.MessageTypeEnum;

import java.util.List;

/**
 * @className: IMessageService
 * @author: yigaohui
 * @date: 2025/2/6 10:09
 * @Version: 1.0
 * @description:
 */

public interface INotifyMessageService {

    /**
     * 消息已读
     *  @param ids 消息id集合
     * @param all 是否全部已读
     * @param messageType 消息类型
     */
    void read(List<Long> ids, boolean all, List<MessageTypeEnum> messageType);
}
