package com.bmos.platform.service.message.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 *
 * @className: MessageTypeEnum
 * @author: yigaohui
 * @date: 2025/1/7 11:09
 * @Version: 1.0
 * @description:
 */

@Getter
@AllArgsConstructor
public enum MessageTypeEnum implements KeyValueEnum<String> {

    AUDIT("AUDIT", "审核消息", "", "MessageTypeEnum.AUDIT.TEMPLATE"),

    MATERIAL_EXPIRE_FORE_WARNING("MATERIAL_EXPIRE_FORE_WARNING", "物料到期预警", "100030001000012", "MessageTypeEnum.MATERIAL_EXPIRE_FORE_WARNING.TEMPLATE"),

    DATA_OUT_LIMIT_WARNING("DATA_OUT_LIMIT_WARNING", "数据超限警告消息", "100030001000013", "MessageTypeEnum.DATA_OUT_LIMIT_WARNING.TEMPLATE"),

    PRODUCT_MODIFY_ABNORMAL_WARNING("PRODUCT_MODIFY_ABNORMAL_WARNING", "生产修订异常警告消息", "100030001000014", "MessageTypeEnum.PRODUCT_MODIFY_ABNORMAL_WARNING.TEMPLATE"),

    EQUIPMENT_DEFAULT_WARNING("EQUIPMENT_DEFAULT_WARNING", "设备故障告警", "100030001000015", "MessageTypeEnum.EQUIPMENT_DEFAULT_WARNING.TEMPLATE"),

    // 集中化

    LISMS_MATERIAL_EXPIRE_WARNING("LISMS_MATERIAL_EXPIRE_WARNING", "物料到期预警", "210060013", "MessageTypeEnum.LISMS_MATERIAL_EXPIRE_WARNING.TEMPLATE"),

    LISMS_MATERIAL_INVENTORY_WARNING("LISMS_MATERIAL_INVENTORY_WARNING", "物料最低库存预警", "210060013", "MessageTypeEnum.LISMS_MATERIAL_INVENTORY_WARNING.TEMPLATE"),

    LISMS_SUPPLIER_EXPIRE_WARNING("LISMS_SUPPLIER_EXPIRE_WARNING", "供应商到期预警", "210060013", "MessageTypeEnum.LISMS_SUPPLIER_EXPIRE_WARNING.TEMPLATE"),

    // 血源

    BSMS_PLASMA_INVENTORY_WARNING("BSMS_PLASMA_INVENTORY_WARNING","血浆库存预警","170040008","MessageTypeEnum.BSMS_PLASMA_INVENTORY_WARNING.TEMPLATE"),

    BSMS_SAMPLE_INVENTORY_WARNING("BSMS_SAMPLE_INVENTORY_WARNING","标本库存预警","170020008","MessageTypeEnum.BSMS_SAMPLE_INVENTORY_WARNING.TEMPLATE");

    @EnumValue
    private final String value;

    private final String name;


    private final String AuthorityCode;

    private final String i18nCode;
}
