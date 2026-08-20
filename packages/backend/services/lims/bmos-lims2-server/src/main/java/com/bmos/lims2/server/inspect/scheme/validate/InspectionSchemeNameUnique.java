package com.bmos.lims2.server.inspect.scheme.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 检验方案名称唯一性校验注解
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = InspectionSchemeNameUniqueValidator.class)
public @interface InspectionSchemeNameUnique {

    String message() default "检验方案名称已存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
} 