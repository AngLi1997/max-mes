package com.bmos.platform.facade.factory.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 房间操作类型状态枚举
 */
@Getter
@AllArgsConstructor
public enum RoomStatusOperateTypeEnum implements CommonEnum<String> {
    /**
     * 人工清场
     */
    MANUAL_INPUT("MANUAL_INPUT","人工清场"),
    /**
     * 生产清场
     */
    AUTO_RECOGNITION("AUTO_RECOGNITION", "生产清场")
    ;
    @EnumValue
    private String code;

    private String desc;

    @Override
    public String getName() {
        return this.desc;
    }

    @Override
    public String getValue() {
        return this.code;
    }
}
