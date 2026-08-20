package com.bmos.lims2.server.eln.record.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RecordStateEnum implements CommonEnum<String> {

    EDIT("编辑","1"),
    AUDIT("审批","2"),
    CERTAIN("启用","3"),
    INVALID("停用","4"),
    CANCEL("作废","5");


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
