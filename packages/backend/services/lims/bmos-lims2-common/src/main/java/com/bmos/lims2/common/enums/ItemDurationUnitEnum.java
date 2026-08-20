package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @className: ItemDurationUnitEnum
 * @author: yigaohui
 * @date: 2025/8/11 9:44
 * @Version: 1.0
 * @description:
 */
@AllArgsConstructor
@Getter
public enum ItemDurationUnitEnum implements KeyValueEnum<String> {

   DAY("DAY", "天"),
   HOUR("HOUR", "时"),
   MINUTE("MINUTE", "分");

   @EnumValue
   private String value;

   private String name;
}
