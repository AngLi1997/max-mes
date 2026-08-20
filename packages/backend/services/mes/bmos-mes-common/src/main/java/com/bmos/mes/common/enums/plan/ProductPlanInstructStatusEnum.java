package com.bmos.mes.common.enums.plan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.CommonEnumVO;
import com.bmos.mes.common.state.StateMachine;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProductPlanInstructStatusEnum implements CommonEnum<String> {

    WAIT_DECOMPOSE("待分解", "WAIT_DECOMPOSE"),
    WAIT_CONFIRM("待确认", "WAIT_CONFIRM"),
    WAIT_SEND("待下发", "WAIT_SEND"),
    SEND("已下发", "SEND"),
    ;
    private final static StateMachine<ProductPlanInstructStatusEnum, ProductPlanInstructStatusEvent> STATE_MACHINE = new StateMachine<>();

    static {
        STATE_MACHINE.accept(WAIT_DECOMPOSE, ProductPlanInstructStatusEvent.WAIT_CONFIRM, WAIT_CONFIRM);
        STATE_MACHINE.accept(WAIT_CONFIRM, ProductPlanInstructStatusEvent.CONFIRM, WAIT_SEND);
        STATE_MACHINE.accept(WAIT_SEND, ProductPlanInstructStatusEvent.SEND, SEND);
    }

    private final String name;
    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static ProductPlanInstructStatusEnum getNextStatus(ProductPlanInstructStatusEnum sourceStatus,
        ProductPlanInstructStatusEvent event) {
        return STATE_MACHINE.getNextStatus(sourceStatus, event);
    }

    @JsonCreator
    public static ProductPlanInstructStatusEnum getEnumByName(CommonEnumVO<String> commonEnumVO) {
        return Arrays.stream(ProductPlanInstructStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(commonEnumVO.getValue()))
                .findFirst()
                .orElse(null);
    }
}
