package com.bmos.mes.common.enums.record;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum ScopeCompareResultEnum implements CommonEnum<String> {

    GREATER_THAN("大于", "GREATER_THAN",">", Collections.singletonList(1), null, 0),
    EQUAL_TO("等于", "EQUAL_TO","==", Collections.singletonList(0), null, null),
    LESS_THAN("小于", "LESS_THAN", "<", Collections.singletonList(-1), 0, null),
    GREATER_AND_EQUAL("大于等于", "GREATER_AND_EQUAL", ">=", Arrays.asList(1, 0), null, 1),
    LESS_AND_EQUAL("小于等于", "LESS_AND_EQUAL","<=", Arrays.asList(-1, 0), 1, null);

    private final String name;
    private final String value;
    private final String symbol;
    private final List<Integer> resultList;

    /**
     * 上限值对应前端枚举value
     */
    private final Integer upperLimitValue;

    /**
     * 下限值对应前端枚举value
     */
    private final Integer lowerLimitValue;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static List<Integer> getResultList(String value) {
        for (ScopeCompareResultEnum e : ScopeCompareResultEnum.values()) {
            if (ObjectUtil.equal(e.getValue(), value)) {
                return e.getResultList();
            }
        }
        return Collections.emptyList();
    }

    public static List<Integer> getResultListByUpperValue(Integer upperValue) {
        for (ScopeCompareResultEnum e : ScopeCompareResultEnum.values()) {
            if (ObjectUtil.equal(e.getUpperLimitValue(), upperValue)) {
                return e.getResultList();
            }
        }
        return Collections.emptyList();
    }

    public static List<Integer> getResultListByLowerValue(Integer lowerValue) {
        for (ScopeCompareResultEnum e : ScopeCompareResultEnum.values()) {
            if (ObjectUtil.equal(e.getLowerLimitValue(), lowerValue)) {
                return e.getResultList();
            }
        }
        return Collections.emptyList();
    }
}
