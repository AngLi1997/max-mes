package com.bmos.mes.common.enums.execute;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 附件类型
 */
@Getter
@AllArgsConstructor
public enum AttachmentTypeEnum implements CommonEnum<String> {

    MODULE_PICTURE("拍照组件附件","MODULE_PICTURE"),
    EVIDENCE_PICTURE("拍照取证附件","EVIDENCE_PICTURE");


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
}
