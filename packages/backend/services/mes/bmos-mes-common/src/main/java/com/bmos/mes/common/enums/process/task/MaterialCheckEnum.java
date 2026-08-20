package com.bmos.mes.common.enums.process.task;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum MaterialCheckEnum implements CommonEnum<String> {

    GREATER_THAN("大于", ">", Collections.singletonList(1)),
    EQUAL_TO("等于", "==", Collections.singletonList(0)),
    LESS_THAN("小于", "<", Collections.singletonList(-1)),
    GREATER_AND_EQUAL("大于等于", ">=", Arrays.asList(1, 0)),
    LESS_AND_EQUAL("小于等于", "<=", Arrays.asList(-1, 0));

    private final String name;
    private final String value;
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
        for (MaterialCheckEnum e : MaterialCheckEnum.values()) {
            if (ObjectUtil.equal(e.getValue(), value)) {
                return e.getResultList();
            }
        }
        return Collections.emptyList();
    }
}
