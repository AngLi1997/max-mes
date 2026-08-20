package com.bmos.lims2.server.inspect.scheme.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 检验方案版本号唯一性校验注解
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = InspectionSchemeVersionNoUniqueValidator.class)
public @interface InspectionSchemeVersionNoUnique {

    String message() default "检验方案版本号已存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String schemeIdField() default "schemeId";

    String versionNoField() default "versionNo";
} 