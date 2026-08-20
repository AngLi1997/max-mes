package com.bmos.mes.service.plan.info.constant;

import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.response.ResponseItem;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.plan.DuplicateKeyUtil;
import org.springframework.dao.DuplicateKeyException;

import java.util.HashMap;
import java.util.Map;

public class PlanConstant {

    private final static String PLAN_NO = "bm_product_plan.uk_planNo";
    private final static String PLAN_NO_1 = "uk_planNo";

    private final static String PROCESSID_BATCHNO = "bm_product_plan.uk_processId_batchNo";
    private final static String PROCESSID_BATCHNO_1 = "uk_processId_batchNo";


    private final static Map<String, ResponseItem> MAP = new HashMap<String, ResponseItem>() {{
        put(PLAN_NO, MesResponseCode.PRODUCT_PLAN_NO_DUPLICATE);
        put(PLAN_NO_1, MesResponseCode.PRODUCT_PLAN_NO_DUPLICATE);
        put(PROCESSID_BATCHNO, MesResponseCode.PRODUCT_PLAN_BATCH_NO_DUPLICATE);
        put(PROCESSID_BATCHNO_1, MesResponseCode.PRODUCT_PLAN_BATCH_NO_DUPLICATE);
    }};

    public static ResponseItem findException(DuplicateKeyException duplicateKeyException) {
        return MAP.getOrDefault(DuplicateKeyUtil.getUniqueIndexName(duplicateKeyException), BaseResponseCode.DUPLICATE_KEY_ERROR);
    }
}
