package com.bmos.mes.service.plan.team.constant;

import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.response.ResponseItem;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.DuplicateKeyUtil;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashMap;
import java.util.Map;

public class TeamConstant {

    private final static String CODE = "uk_code";


    private final static Map<String, ResponseItem> MAP = new HashMap<String, ResponseItem>() {{
        put(CODE, MesResponseCode.TEAM_CODE_EXISTS);
    }};

    public static ResponseItem findException(DuplicateKeyException duplicateKeyException) {
        return MAP.getOrDefault(DuplicateKeyUtil.getUniqueIndexName(duplicateKeyException), BaseResponseCode.DUPLICATE_KEY_ERROR);
    }
}
