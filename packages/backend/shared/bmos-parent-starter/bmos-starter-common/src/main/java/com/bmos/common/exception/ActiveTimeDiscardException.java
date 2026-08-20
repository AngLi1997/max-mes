package com.bmos.common.exception;

import com.bmos.common.response.ResponseItem;

public class ActiveTimeDiscardException extends RuntimeException {
    public ResponseItem getResponseItem() {
        return BaseResponseCode.ACTIVE_TIME_DISCARD;
    }
}
