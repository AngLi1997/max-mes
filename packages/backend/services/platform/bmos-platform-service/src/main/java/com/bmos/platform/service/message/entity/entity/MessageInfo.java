package com.bmos.platform.service.message.entity.entity;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * (BmMessageInfo)实体类
 *
 * @author makejava
 * @since 2025-01-08 15:42:52
 */
@TableName("bp_message_info")
@Data
public class MessageInfo extends BaseDO {
    private static final long serialVersionUID = -52446261932800833L;
    /**
     * 消息类型
     */
    private MessageTypeEnum msgType;
    /**
     * 发送人
     */
    private String sendId;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
}

