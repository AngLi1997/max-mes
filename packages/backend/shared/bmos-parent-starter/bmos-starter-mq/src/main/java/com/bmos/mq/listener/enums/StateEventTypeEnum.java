package com.bmos.mq.listener.enums;

import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum StateEventTypeEnum implements KeyValueEnum<String> {

    EQUIPMENT("设备状态更新", "equipment"),
    ROOM("房间状态更新", "room");

    private String name;

    private String code;


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}
