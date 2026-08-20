package com.bmos.mes.mq.exception;

/**
 * @Author yigaohui
 * @Description MqException
 * @Date 2023/7/21 10:35
 */
public class MqException extends RuntimeException {

    public MqException(Exception e) {
        super(e);
    }

    public MqException(String message) {
        super(new RuntimeException(message));
    }

    public MqException(String message, Exception e) {
        super(new Exception(message, e));
    }


}
