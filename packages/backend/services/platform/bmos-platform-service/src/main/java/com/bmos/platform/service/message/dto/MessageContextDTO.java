package com.bmos.platform.service.message.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息上下文
 *
 * @className: Context
 * @author: yigaohui
 * @date: 2025/1/8 14:10
 * @Version: 1.0
 * @description:
 */

@Data
public class MessageContextDTO {
    private LocalDateTime time;

    private List<String> notifyUserIds;
}
