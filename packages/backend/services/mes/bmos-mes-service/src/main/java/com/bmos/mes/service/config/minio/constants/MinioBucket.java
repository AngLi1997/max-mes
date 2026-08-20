package com.bmos.mes.service.config.minio.constants;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/13 17:30
 */
public enum MinioBucket {

    /**
     * 归档
     */
    ARCHIVE_BUCKET,

    /**
     * 批记录
     */
    RECORD_BUCKET,

    /**
     * 操作规程
     */
    OPERATE_RULE_SOP,

    /**
     * 执行
     */
    BMOS_PRODUCT,

    /**
     * 批记录模板
     */
    BMOS_BATCH_TEMPLATE,

    /**
     * 批签发数据
     */
    BMOS_LOT_RELEASE;
}
