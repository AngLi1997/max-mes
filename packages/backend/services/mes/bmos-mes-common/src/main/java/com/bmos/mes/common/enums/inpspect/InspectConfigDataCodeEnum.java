package com.bmos.mes.common.enums.inpspect;

import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 检测配置数据枚举
 */
@Getter
@AllArgsConstructor
public enum InspectConfigDataCodeEnum implements CommonEnum<String> {
    PLEASE_CHECK_NO("请验单号", "pleaseCheckNo"),
    INSTRUCTION_NO("指令单编号", "instructionNo"),
    PRODUCT_BATCH_NO("生产批号", "productBatchNo"),
    PRODUCT_LINE_NAME("产线名称", "productLineName"),
    PRODUCT_LINE_CODE("产线编码", "productLineCode"),
    PRODUCT_BATCH_QUANTITY("生产批量", "productBatchQuantity"),
    PRODUCT_BATCH_QUANTITY_UNIT("生产批量单位", "productBatchQuantityUnit"),
    MATERIAL_NAME("物料名称", "materialName"),
    MATERIAL_CODE("物料编码", "materialCode"),
    MATERIAL_BATCH_NO("物料批号", "materialBatchNo"),
    SAMPLE_QUANTITY("取样量", "sampleQuantity"),
    SAMPLE_QUANTITY_UNIT("取样量单位", "sampleQuantityUnit"),
    INSPECTOR("请验人", "inspector")
    ;
    private String name;

    private String value;
}
