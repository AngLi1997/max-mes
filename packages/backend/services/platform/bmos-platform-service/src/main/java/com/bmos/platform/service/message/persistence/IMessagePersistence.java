package com.bmos.platform.service.message.persistence;

import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.message.constants.MessageStatusEnum;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.MessageCountDTO;
import com.bmos.platform.service.message.dto.MessageDTO;
import com.bmos.platform.service.message.dto.MessageInfoDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 消息持久化接口
 *
 * @className: IMessagePersistence
 * @author: yigaohui
 * @date: 2025/1/8 15:48
 * @Version: 1.0
 * @description:
 */

public interface IMessagePersistence {
    /**
     * 持久化消息
     *
     * @param sender         发送人
     * @param messageType
     * @param receiveUserIds 接收人id
     * @param message        消息内容
     */
    void saveMessage(String sender, MessageTypeEnum messageType, Collection<String> receiveUserIds, MessageDTO message);

    /**
     * 查询消息数量统计
     *
     * @return 结果
     */
    Map<String, List<MessageCountDTO>> selectNotReadCount(Collection<String> userIds);

    /**
     * 分页查询
     *
     * @param userId        用户id
     * @param messageType   消息类型
     * @param messageStatus 消息状态
     * @param page          分页信息
     * @return 查询结果
     */
    CommonPage<MessageInfoDTO> selectMessagePage(String userId, List<MessageTypeEnum> messageType, MessageStatusEnum messageStatus, BasePage page);

    /**
     * 更新已读状态
     *  @param messageIds 消息id集合
     * @param all 是否全部已读
     * @param messageType
     */
    void updateReadStatus(List<Long> messageIds, boolean all, List<MessageTypeEnum> messageType);
}
