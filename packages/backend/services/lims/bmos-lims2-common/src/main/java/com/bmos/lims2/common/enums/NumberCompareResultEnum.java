package com.bmos.lims2.common.enums;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum NumberCompareResultEnum implements CommonEnum<String> {

    GREATER_THAN("大于", "GREATER_THAN",">", Collections.singletonList(1)),
    EQUAL_TO("等于", "EQUAL_TO","==", Collections.singletonList(0)),
    LESS_THAN("小于", "LESS_THAN", "<", Collections.singletonList(-1)),
    GREATER_AND_EQUAL("大于等于", "GREATER_AND_EQUAL", ">=", Arrays.asList(1, 0)),
    LESS_AND_EQUAL("小于等于", "LESS_AND_EQUAL","<=", Arrays.asList(-1, 0));

    private final String name;
    private final String value;
    private final String symbol;
    private final List<Integer> resultList;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static List<Integer> getResultList(String value) {
        for (NumberCompareResultEnum e : NumberCompareResultEnum.values()) {
            if (ObjectUtil.equal(e.getValue(), value)) {
                return e.getResultList();
            }
        }
        return Collections.emptyList();
    }
}
