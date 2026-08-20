package com.bmos.platform.common.enums.tag;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @className: BarcodeFormat
 * @author: yigaohui
 * @date: 2025/9/22 14:54
 * @Version: 1.0
 * @description:
 */

@Getter
@AllArgsConstructor
public enum BarcodeFormat implements CommonEnum<String> {

    QRCODE("二维码", "QR_CODE"),
    CODE_128("一维码", "CODE_128");


    private final String name;

    @EnumValue
    private final String value;
}
