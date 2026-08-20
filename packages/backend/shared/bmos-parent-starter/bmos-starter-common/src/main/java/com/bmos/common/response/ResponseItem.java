package com.bmos.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseItem {

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误信息
     */
    private String message;

    @JsonIgnore
    private Object[] args;
    /**
     * 模块名
     */
    private String modelName;

    public ResponseItem(int code, String message, String modelName) {
        this.code = code;
        this.message = message;
        this.modelName = modelName;
    }

    public static ResponseItem from(int code, String message, String modelName) {
        return new ResponseItem(code, message, modelName);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public ResponseItem args(Object... args){
        this.args = args;
        return this;
    }

    public Object[] getArgs(){
        return this.args;
    }
}
