package com.bmos.mes.common.enums.plan;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;

/**
 * 模板版本状态枚举
 * 对应国际化的code：8302xx
 */
@AllArgsConstructor
public enum BatchRecordArchiveStatusEnum implements CommonEnum<Integer> {

    EDIT(830401,"编辑"),
    AUDIT(830402,"审批中"),
    EFFECTIVE(830403 ,"生效"),
    SCRAP(830404,"作废"),
    ;

    private Integer value;

    private String name;


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public static BatchRecordArchiveStatusEnum getEnumByValue(Integer value) {
        for (BatchRecordArchiveStatusEnum item : BatchRecordArchiveStatusEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }
}
