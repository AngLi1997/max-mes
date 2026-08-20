package com.bmos.mes.common.constant;

public interface ProcessConstant {
    /**
     * 默认版本号
     */
    String defaultVersion = "V1";

    /**
     * 工艺配置 配置json 工位字段常量
     */
    String stationField = "station";

    String stationPathField = "stationShow";

    String roomField = "roomIdList";

    /**
     * 任务重做标识
     */
    String restart = "restart";

    /**
     * 已结束标识
     */
    String IS_END = "isEnd";

    /**
     * 任务强制激活标识
     */
    String COERCE_ACTIVE = "coerceActive";

    String PROCESS_KEY_HANDEL = "handelStepIdOrTaskId";

    /**
     * 复用工步id
     */
    Long REUSE_PROCEDURE_STEP_ID = 0L;
}
