package com.bmos.mes.service.components.annotations;

import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;

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
public @interface BmosComponentDetail {

    /**
     * 组件名称
     * @return
     */
    BusinessComponentTypeEnum value();

    /**
     * 填写时间戳
     * @return true 保存时间戳 false 不保存时间戳
     */
    boolean fillTimestamp() default false;
}
