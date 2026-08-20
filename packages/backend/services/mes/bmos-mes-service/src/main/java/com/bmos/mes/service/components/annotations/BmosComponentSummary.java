package com.bmos.mes.service.components.annotations;

import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.components.enums.BmosComponentSummaryType;

import java.lang.annotation.*;

/**
 * 业务组件字段
 * @author liang
 * @version 1.0.0
 * @date 2024/7/30 10:44
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BmosComponentSummary {

    /**
     * 组件名称
     * @return
     */
    BusinessComponentTypeEnum[] value();

    /**
     * 统计类型
     * size：总数
     * sum：总和
     * value：直接回显值
     * @return
     */
    BmosComponentSummaryType[] summaryType();
}
