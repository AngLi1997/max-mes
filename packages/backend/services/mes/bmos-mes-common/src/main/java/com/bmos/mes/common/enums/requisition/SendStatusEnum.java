package com.bmos.mes.common.enums.requisition;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 仓库发料状态
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 18:11
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@AllArgsConstructor
public enum SendStatusEnum implements CommonEnum<Integer> {

    /**
     * 未发料
     */
    NOT_SEND(0, "未发料"),

    /**
     * 发料完成
     */
    COMPLETED_SEND(1, "发料完成"),

    /**
     * 取消发料
     */
    CANCEL_SEND(2, "取消发料");


    @EnumValue
    private final Integer value;

    private final String name;
}
