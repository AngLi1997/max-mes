package com.bmos.platform.service.message.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.message.constants.MessageStatusEnum;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.entity.entity.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @className: MessageMapper
 * @author: yigaohui
 * @date: 2025/1/8 15:46
 * @Version: 1.0
 * @description:
 */

@Mapper
public interface MessageInfoMapper extends BaseMapperX<MessageInfo> {
    /**
     * 查询用户消息列表
     *
     * @param userId        用户id
     * @param messageType   消息类型
     * @param messageStatus 消息状态
     * @return 查询结果
     */
    List<MessageInfo> selectUserMessageList(@Param("userId") String userId, @Param("messageType") List<MessageTypeEnum> messageType, @Param("messageStatus") MessageStatusEnum messageStatus);
}
