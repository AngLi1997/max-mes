package com.bmos.lims2.server.platform.util;

import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.response.ResponseItem;
import com.bmos.common.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

@Slf4j
public class FeignUtils {
    public static <T, E> ResponseInfo<T> handleRequest(Function<E, ResponseInfo<T>> function, E e) {
        ResponseInfo<T> responseInfo;
        try {
            responseInfo = function.apply(e);
        } catch (Exception exception) {
            log.error("调用feign失败：{}", exception);
            throw new BmosException(BaseResponseCode.SERVER_EXCEPTION);
        }
        if (responseInfo.isError()) {
            log.error(JsonUtils.toJsonString(responseInfo));
            throw new BmosException(new ResponseItem(responseInfo.getCode(), responseInfo.getMessage(), "feign调用"));
        }
        return responseInfo;
    }
}
