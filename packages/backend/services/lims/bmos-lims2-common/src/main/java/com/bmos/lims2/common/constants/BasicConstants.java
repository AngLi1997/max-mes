package com.bmos.lims2.common.constants;

public interface BasicConstants {

    String DEFAULT_SORTING_FIELD = "create_time desc, id desc";

    /**
     * 进行中请验单默认排序
     */
    String DEFAULT_CHECK_ORDER_SORTING = "verify_time desc";

    /**
     * 默认检验单编号规则code
     */
    String DEFAULT_ORDER_RULE_CODE = "lims.inspect.order.code";

    /**
     * 默认样品编号规则code
     */
    String DEFAULT_SAMPLE_RULE_CODE = "lims.inspect.sample.code";

    /**
     * 一天的默认开始时间
     */
    String DEFAULT_START_TIME = " 00:00:00";

    /**
     * 一天的默认结束时间
     */
    String DEFAULT_END_TIME = " 23:59:59";

    String TERMINATE_LOG_NAME = "终止";

    String MATERIAL_PAGE_ORDER = "merge_code asc";

    Long PARENT_ID = 0L;
}
