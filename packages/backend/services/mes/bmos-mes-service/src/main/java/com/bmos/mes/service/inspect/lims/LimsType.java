package com.bmos.mes.service.inspect.lims;

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
