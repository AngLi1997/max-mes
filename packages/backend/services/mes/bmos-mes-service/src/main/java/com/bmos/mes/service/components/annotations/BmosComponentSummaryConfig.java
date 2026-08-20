package com.bmos.mes.service.components.annotations;

import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.components.enums.BmosComponentSummaryConfigFilter;

import java.lang.annotation.*;

/**
 * 业务汇总规则
 * @author liang
 * @version 1.0.0
 * @date 2024/7/30 10:44
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BmosComponentSummaryConfig {

    /**
     * 配置过滤规则
     * @return
     */
    BmosComponentSummaryConfigFilter filter();

    /**
     * 组件名称
     * @return
     */
    BusinessComponentTypeEnum value();
}
