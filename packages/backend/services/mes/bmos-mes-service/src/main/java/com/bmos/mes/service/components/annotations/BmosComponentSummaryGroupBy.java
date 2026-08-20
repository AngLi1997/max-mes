package com.bmos.mes.service.components.annotations;

import java.lang.annotation.*;

/**
 * 业务组件分组字段
 * @author liang
 * @version 1.0.0
 * @date 2024/7/30 10:44
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BmosComponentSummaryGroupBy {

}
