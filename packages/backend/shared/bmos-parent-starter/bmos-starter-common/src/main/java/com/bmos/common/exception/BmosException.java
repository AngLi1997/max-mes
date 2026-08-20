package com.bmos.common.exception;

import com.bmos.common.response.ResponseItem;

import java.text.MessageFormat;


public class BmosException extends RuntimeException {

    private final ResponseItem responseItem;

    public BmosException(ResponseItem responseItem) {
        super(responseItem.getMessage());
        this.responseItem = responseItem;
    }


    public BmosException(ResponseItem responseItem, String... params) {
        super(MessageFormat.format(responseItem.getMessage(), params));
        responseItem.args(params);
        this.responseItem = responseItem;
    }

    public ResponseItem getResponseItem() {
        return responseItem;
    }
}
