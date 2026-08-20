package com.bmos.platform.service.message.persistence;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mybatis.page.BasePage;
import com.bmos.mybatis.page.CommonPage;
import com.bmos.platform.service.message.constants.MessageStatusEnum;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.bmos.platform.service.message.dto.MessageCountDTO;
import com.bmos.platform.service.message.dto.MessageDTO;
import com.bmos.platform.service.message.dto.MessageInfoDTO;
import com.bmos.platform.service.message.entity.entity.MessageInfo;
import com.bmos.platform.service.message.entity.entity.MessageUser;
import com.bmos.platform.service.message.mapper.MessageInfoMapper;
import com.bmos.platform.service.message.mapper.MessageUserMapper;
import com.bmos.platform.service.util.PageUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 默认持久化
 * 放到mysql表中
 *
 * @className: DefaultPersistence
 * @author: yigaohui
 * @date: 2025/1/8 15:49
 * @Version: 1.0
 * @description:
 */

@Service
public class DefaultPersistence implements IMessagePersistence {

    @Autowired
    private MessageInfoMapper messageInfoMapper;

    @Autowired
    private MessageUserMapper messageUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(String sender, MessageTypeEnum messageType, Collection<String> receiveUserIds, MessageDTO message) {
        // 如果通知人为空，则不进行持久化
        if (CollectionUtil.isEmpty(receiveUserIds)) {
            return;
        }
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMsgType(messageType);
        messageInfo.setMsgContent(JSONUtil.toJsonStr(message));
        messageInfo.setSendTime(LocalDateTime.now());
        messageInfo.setSendId(sender);
        messageInfo.setId(IdUtil.getSnowflakeNextId());
        // 系统自动发送的消息，存储时创建人和更新人设置成system
        if (SysUserHolder.getUser().getUserId() == null) {
            messageInfo.setCreateBy("system");
            messageInfo.setUpdateBy("system");
        }
        messageInfoMapper.insert(messageInfo);


        messageUserMapper.insertBatch(receiveUserIds.stream().distinct().map(receiveUserId -> {
            MessageUser bmMessageUser = new MessageUser();
            bmMessageUser.setId(IdUtil.getSnowflakeNextId());
            bmMessageUser.setMessageId(messageInfo.getId());
            bmMessageUser.setUserId(receiveUserId);
            bmMessageUser.setMsgStatus(MessageStatusEnum.NOT_READ);
            bmMessageUser.setMsgType(messageInfo.getMsgType());
            return bmMessageUser;
        }).collect(Collectors.toList()));
    }

    @Override
    public Map<String, List<MessageCountDTO>> selectNotReadCount(Collection<String> userIds) {
        LambdaQueryWrapper<MessageUser> lambda = new QueryWrapper<MessageUser>().lambda();
        lambda.eq(MessageUser::getMsgStatus, MessageStatusEnum.NOT_READ);
        lambda.eq(MessageUser::getDeleted, false);
        lambda.in(MessageUser::getUserId, userIds);
        // 按照用户和消息类型分组
        List<MessageUser> messageUsers = messageUserMapper.selectList(lambda);
        HashMap<String, List<MessageCountDTO>> hashMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(messageUsers)) {
            Map<String, List<MessageUser>> collect = messageUsers.stream().collect(Collectors.groupingBy(MessageUser::getUserId));
            collect.forEach((key, value) -> {
                if (CollectionUtil.isNotEmpty(value)) {
                    Map<MessageTypeEnum, List<MessageUser>> messageTypeMap = value.stream().collect(Collectors.groupingBy(MessageUser::getMsgType));
                    List<MessageCountDTO> countDTOS = messageTypeMap.entrySet().stream().map(messageTypeEntry -> {
                        MessageCountDTO messageCountDTO = new MessageCountDTO();
                        messageCountDTO.setMessageType(messageTypeEntry.getKey());
                        messageCountDTO.setCount(messageTypeEntry.getValue().size());
                        return messageCountDTO;
                    }).collect(Collectors.toList());
                    hashMap.put(key, countDTOS);
                }
            });
        }
        return hashMap;
    }


    @Override
    public CommonPage<MessageInfoDTO> selectMessagePage(String userId, List<MessageTypeEnum> messageType, MessageStatusEnum messageStatus, BasePage page) {
//        LambdaQueryWrapper<MessageUser> ql = new QueryWrapper<MessageUser>().lambda()
//                .eq(MessageUser::getDeleted, false)
//                .in(CollectionUtil.isNotEmpty(messageType), MessageUser::getMsgType, messageType)
//                .eq(MessageUser::getUserId, userId)
//                .eq(messageStatus != null, MessageUser::getMsgStatus, messageStatus);
//        List<MessageUser> messageUsers = messageUserMapper.selectList(ql);
//        if (CollectionUtil.isEmpty(messageUsers)) {
//            return new CommonPage<>();
//        }
//        List<Long> messageIds = messageUsers.stream().map(MessageUser::getMessageId).collect(Collectors.toList());
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), PageUtils.getOrderByOrDefaultByUpdateTimeDesc(page));
        List<MessageInfo> messageInfos = messageInfoMapper.selectUserMessageList(userId, messageType, messageStatus);
        return CommonPage.convertPage(messageInfos, list -> BeanUtil.copyToList(list, MessageInfoDTO.class));
    }


    @Override
    public void updateReadStatus(List<Long> ids, boolean all, List<MessageTypeEnum> messageType) {
        LambdaUpdateWrapper<MessageUser> lambda = new UpdateWrapper<MessageUser>().lambda();
        lambda.eq(MessageUser::getUserId, SysUserHolder.getUser().getUserId());
        lambda.eq(MessageUser::getMsgStatus, MessageStatusEnum.NOT_READ);
        lambda.eq(MessageUser::getDeleted, false);
        lambda.in(messageType != null, MessageUser::getMsgType, messageType);
        lambda.in(!all && CollectionUtil.isNotEmpty(ids), MessageUser::getMessageId, ids);
        lambda.set(MessageUser::getMsgStatus, MessageStatusEnum.READ);
        messageUserMapper.update(null, lambda);
    }
}
