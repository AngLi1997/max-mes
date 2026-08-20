package com.bmos.lims2.server.eln.record.enums;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum CalculateTypeEnum implements CommonEnum<String> {

    DD_HH_MM_SS("dd HH:mm:ss", Arrays.asList("day", "hour", "minute","second"), null, null),
    HH_MM_SS("HH:mm:ss", Arrays.asList("hour", "minute","second"), null, null),
    MINUTE_SS("mm:ss", Arrays.asList("minute","second"), "hour", "minute"),
    SECOND("ss", Arrays.asList("second"), "", ""),
    DD_HH_MM("dd HH:mm", Arrays.asList("day", "hour", "minute"), "second", "minute"),
    DD_HH("dd HH",Arrays.asList("day","hour"),"minute", "hour"),
    DD("dd", Arrays.asList("day"), "hour", "day"),
    HH_MM("HH:mm", Arrays.asList("hour", "minute"), "second", "minute"),
    HH("HH", Arrays.asList("hour"), "minute", "hour"),
    MINUTE("mm", Arrays.asList("minute"), "second", "minute");


    private final String name;

    private final List<String> type;

    //根据那个单位修约
    private final String fetch;

    //修约字段
    private final String deposit;


    @Override
    public String getValue() {
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public static CalculateTypeEnum getByType(String value) {
        for (CalculateTypeEnum e : CalculateTypeEnum.values()) {
            if (ObjectUtil.equal(e.getName(), value)) {
                return e;
            }
        }
        return null;
    }
}
