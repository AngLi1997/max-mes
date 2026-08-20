package com.bmos.unit.exception;

/**
 * 单位转换异常
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/29 10:28
 */
public class UnitConvertException extends RuntimeException {

    public UnitConvertException() {
        super("单位转换异常");
    }

    public UnitConvertException(String message) {
        super(message);
    }
}
