package com.bmos.common.exporter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UploadValidation {

    String[] fileTypes() default {"xls", "xlsx"};

    long maxFileSize() default 1024 * 1024 * 1024;
}
