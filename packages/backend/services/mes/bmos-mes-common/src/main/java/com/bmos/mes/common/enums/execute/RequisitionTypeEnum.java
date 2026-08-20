package com.bmos.mes.common.enums.execute;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequisitionTypeEnum implements CommonEnum<String> {

    BATCH_QUANTITY_PICK("按批次量领料", "BATCH_QUANTITY_PICK", 1),
    MATERIAL_QUANTITY_PICK("按物料量领料", "MATERIAL_QUANTITY_PICK", 2);

    private final String name;
    @EnumValue
    private final String value;

    private final Integer mappingValue;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static RequisitionTypeEnum getByValue(String value) {
        for (RequisitionTypeEnum e : RequisitionTypeEnum.values()) {
            if (ObjectUtil.equal(e.value, value)) {
                return e;
            }
        }
        return null;
    }
}
