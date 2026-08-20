package com.bmos.platform.service.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import lombok.Data;

/**
 * @className: MessageTemplate
 * @author: yigaohui
 * @date: 2025/1/8 11:37
 * @Version: 1.0
 * @description:
 */
@TableName("bp_message_template")
@Data
public class MessageTemplate extends BaseDO {
    private String titleTemplate;

    private String contentTemplate;

    private MessageTypeEnum messageType;
}
