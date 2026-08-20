package com.bmos.wms.service.inspect.lims;

/**
 * LIMS 对接类型（与 MES LimsType 同语义）。
 *
 * <p>WMS 当前只支持 BMOS；THIRD_PARTY 保留枚举值，但 selector 会显式抛错。
 */
public enum LimsType {
    THIRD_PARTY,
    BMOS;

    public static LimsType of(String v) {
        if (v == null) {
            return THIRD_PARTY;
        }
        try {
            return LimsType.valueOf(v.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return THIRD_PARTY;
        }
    }
}
