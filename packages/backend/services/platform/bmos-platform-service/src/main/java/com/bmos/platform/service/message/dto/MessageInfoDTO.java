package com.bmos.platform.service.message.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.platform.service.message.constants.MessageTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * (BmMessageInfo)实体类
 *
 * @author makejava
 * @since 2025-01-08 15:42:52
 */
@Data
public class MessageInfoDTO extends BaseDO {
    private Long id;
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


    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;
}

