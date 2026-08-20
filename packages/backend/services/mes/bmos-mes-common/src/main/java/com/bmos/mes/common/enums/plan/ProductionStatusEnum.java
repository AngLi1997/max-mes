package com.bmos.mes.common.enums.plan;

import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProductionStatusEnum implements CommonEnum<String> {

    NOT_ISSUED("NOT_ISSUED", "未下发", ProductPlanStartEnum.WAIT,
            Lists.newArrayList(ProductPlanInstructStatusEnum.WAIT_CONFIRM,
                    ProductPlanInstructStatusEnum.WAIT_DECOMPOSE,
                    ProductPlanInstructStatusEnum.WAIT_SEND), false),

    ISSUED("ISSUED", "已下发", ProductPlanStartEnum.WAIT,
            Collections.singletonList(ProductPlanInstructStatusEnum.SEND),
            false),

    DURING_PRODUCTION("DURING_PRODUCTION", "生产中", ProductPlanStartEnum.STARTING,
            Collections.singletonList(ProductPlanInstructStatusEnum.SEND), false),

    PRODUCTION_COMPLETED("PRODUCTION_COMPLETED", "生产完成", ProductPlanStartEnum.END,
            Collections.singletonList(ProductPlanInstructStatusEnum.SEND), false),

    PRODUCTION_TERMINATION("PRODUCTION_TERMINATION", "生产终止", ProductPlanStartEnum.TERMINATION,
            Collections.singletonList(ProductPlanInstructStatusEnum.SEND), false),

    PRODUCTION_PAUSED("PRODUCTION_PAUSED", "生产暂停", ProductPlanStartEnum.STARTING,
            Collections.singletonList(ProductPlanInstructStatusEnum.SEND), true);

    private final String value;

    private final String name;

    private final ProductPlanStartEnum isStart;

    private final List<ProductPlanInstructStatusEnum> instructStatus;

    private final Boolean paused;

    @JsonCreator
    public static ProductionStatusEnum getEnumByValue(String value) {
        return Arrays.stream(ProductionStatusEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

    public static ProductionStatusEnum getByMappingEnum(ProductPlanStartEnum isStart,
                                                        ProductPlanInstructStatusEnum insStatus, Boolean paused) {
        if (!Objects.equals(insStatus, ProductPlanInstructStatusEnum.SEND)) {
            return NOT_ISSUED;
        }
        if (BooleanUtil.isTrue(paused)) {
            return PRODUCTION_PAUSED;
        }
        Boolean finalPaused = false;
        return Arrays.stream(ProductionStatusEnum.values())
                .filter(e -> Objects.equals(e.isStart, isStart) && Objects.equals(e.paused, finalPaused) && e.instructStatus.contains(insStatus))
                .findFirst()
                .orElse(null);
    }

}
