package com.bmos.logging.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    /**
     * 默认值:getRemark
     * 示例:
     * 若备注的成员变量名为remarkTest
     * 则获取该值的方法为getRemarkTest
     *
     * @return 获取remark的方法名
     */
    String remark() default "getRemark";

    /**
     * 需要过滤字段的对象类全名
     *
     * @return
     */
    String filterArgType() default "";

    /**
     * 过滤对象不需要保存的字段名数组
     *
     * @return
     */
    String[] filterFields() default "";
}
