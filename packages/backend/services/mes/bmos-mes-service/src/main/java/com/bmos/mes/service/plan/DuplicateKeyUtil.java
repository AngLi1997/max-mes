package com.bmos.mes.service.plan;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateKeyUtil {
    public static String getUniqueIndexName(DuplicateKeyException duplicateKeyException) {
        return duplicateKeyException.getCause().getMessage().split("'")[3];
    }
}
