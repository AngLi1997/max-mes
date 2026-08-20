package com.bmos.adaptor.config;

import cn.hutool.core.io.IoUtil;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class BmosOpenFeignErrorDecoder implements ErrorDecoder {

    private final Logger log = LoggerFactory.getLogger(ErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = IoUtil.read(response.body().asReader(StandardCharsets.UTF_8));
            log.error("feign 调用错误：{}", body);
            return new BmosException(BaseResponseCode.FEIGN_REMOTE_CALL_ERROR);
        } catch (IOException e) {
            log.error("读取响应异常：{}", e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.FEIGN_RESPONSE_READ_ERROR);
        }
    }
}
