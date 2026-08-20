package com.bmos.mes.service.record.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RecordStateEnum implements CommonEnum<String> {

    EDIT("编辑","1"),
    AUDIT("审批","2"),
    CERTAIN("确定","3"),
    CANCEL("作废","4");


    private String code;
    @EnumValue
    private String value;


    @Override
    public String getName() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
