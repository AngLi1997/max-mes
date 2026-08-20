package com.bmos.mes.service.execute.constant;

public interface ExecuteFormDataConstant {

    /**
     * 公式计算时产生的值得默认工序步骤id
     */
    Long FORMULA_PROCEDURE_STEP_ID = 0L;


    /**
     * 默认复制版本号
     */
    Long DEFAULT_COPY_VERSION = 0L;

    /**
     * 计算时若无copyVersion的默认值
     * 处于该默认值的值需要在保存前过滤
     */
    Long CALCULATE_DEFAULT_COPY_VERSION = (long) Integer.MAX_VALUE;

    /**
     * 系统计算值填充操作用户
     */
    String OPERATION_USER_SYSTEM = "system";
}
