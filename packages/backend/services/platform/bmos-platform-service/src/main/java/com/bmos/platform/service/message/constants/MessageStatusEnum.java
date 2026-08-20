package com.bmos.platform.service.message.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 *
 * @className: MessageTypeEnum
 * @author: yigaohui
 * @date: 2025/1/7 11:09
 * @Version: 1.0
 * @description:
 */

@Getter
@AllArgsConstructor
public enum MessageStatusEnum implements KeyValueEnum<String> {

    NOT_READ("NOT_READ", "未读"),

    READ("READ", "已读");
    @EnumValue
    private String value;

    private String name;
}
