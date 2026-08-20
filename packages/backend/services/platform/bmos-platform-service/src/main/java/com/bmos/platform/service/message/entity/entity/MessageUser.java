package com.bmos.platform.service.message.entity.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.service.message.constants.MessageStatusEnum;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (BmMessageUser)实体类
 *
 * @author makejava
 * @since 2025-01-08 15:42:53
 */
@Data
@TableName("bp_message_user")
public class MessageUser extends BaseDO {
    private static final long serialVersionUID = -85524564359809819L;
    /**
     * 消息主表id
     */
    private Long messageId;
    /**
     * 接收人
     */
    private String userId;
    /**
     * 消息状态
     */
    private MessageStatusEnum msgStatus;

    /**
     * 消息类型
     */

    private MessageTypeEnum msgType;

}

