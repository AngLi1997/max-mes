package com.bmos.lims2.common.constants;


/**
 * 记录配置分类常量
 */
public interface RecordConstant {

    Integer ZERO = 0;

    Long PARENT_ID = 0L;

    Integer ONE = 1;

    String FILE_TYPE = ".docx";

    String COMPLIE_CODE = "1";

    String AUDIT_CODE = "2";

    Integer EXECUTORS_SIZE = 3;

    Integer DISCARD = 3;

    Integer EXECUTORS_MAX = 9;

    Integer PRODUCTION_ID_MAX = 200;

    String TEMPORARY_FOLDER = "temp";

    String REDISSON_KEY = "saveFormula";

    String UPLOAD = ".pdf";

    String REDISSON_KEY_SAVE_COMPONENT = "saveComponent";

    /**
     * 批记录生成失败后path值
     */
    String ERROR_PATH = "ERROR";
}
