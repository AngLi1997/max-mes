package com.bmos.logging.annotation.defined;

import java.lang.annotation.*;

/**
 * @author renjinguang
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationUserDefined {

    /**
     * 备注 取值表达式
     * 如: #id,#param.id
     */
    String remark() default "";

    /**
     * 操作对象
     */
    String operationObject();
}
