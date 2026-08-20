package com.bmos.platform.facade.dict.enums;

/**
 * 字典编码常量
 * 100040002001, '物料信息自定义字段', 'MaterialCustomFields'
 * 100040002002, '物料批次自定义字段', 'MaterialBatchCustomFields'
 * 100040002003, '物料件自定义字段', 'MaterialPieceCustomFields'
 */
public interface DictCodeConstants {

    /**
     * 物料信息自定义字段
     */
    String MATERIAL_CUSTOM_FIELDS = "MaterialCustomFields";

    /**
     * 物料批次自定义字段
     */
    String MATERIAL_BATCH_CUSTOM_FIELDS = "MaterialBatchCustomFields";

    /**
     * 物料件自定义字段
     */
    String MATERIAL_PIECE_CUSTOM_FIELDS = "MaterialPieceCustomFields";

    /**
     * 生产批次参数的数据键值
     */
    String PRODUCT_BATCH_NO_PARAMETER = "batchNo";

}
