package com.bmos.common.exception;

import com.bmos.common.response.ResponseItem;

public class ActiveException extends RuntimeException {
    public ResponseItem getResponseItem() {
        return BaseResponseCode.UN_ACTIVE;
    }
}
