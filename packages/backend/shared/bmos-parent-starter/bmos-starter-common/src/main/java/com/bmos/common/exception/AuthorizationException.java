package com.bmos.common.exception;

import com.bmos.common.response.ResponseItem;

public class AuthorizationException extends RuntimeException {
    public ResponseItem getResponseItem() {
        return BaseResponseCode.UN_AUTHORIZATION;
    }
}
