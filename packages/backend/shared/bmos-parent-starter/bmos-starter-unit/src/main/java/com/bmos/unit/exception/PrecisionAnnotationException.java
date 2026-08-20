package com.bmos.unit.exception;

/**
 * 精度修约注解异常
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/29 10:28
 */
public class PrecisionAnnotationException extends RuntimeException {

    public PrecisionAnnotationException() {
        super("精度修约注解异常");
    }

    public PrecisionAnnotationException(String message) {
        super(message);
    }
}
