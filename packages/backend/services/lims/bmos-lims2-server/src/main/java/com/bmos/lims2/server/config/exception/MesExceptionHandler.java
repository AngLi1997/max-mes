package com.bmos.lims2.server.config.exception;

import cn.hutool.core.util.StrUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.orchestrator.engine.core.exception.InfiniteEngineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.bmos.lims2.common.i18n.LimsResponseCode.FLOW_AUDIT_ERROR;

@RestControllerAdvice
@Slf4j
public class MesExceptionHandler {

    private final static String ERROR = "解析元素失败:%s";

    @ExceptionHandler({InfiniteEngineException.class})
    public ResponseInfo<?> authorizationException(InfiniteEngineException ex) {
        if (StrUtil.equals(ex.getMessage(),ERROR)){
            return ResponseInfo.failure(LimsResponseCode.ORCHESTRATOR_ENGINE_ERROR, ex.getMessage(), ex.getProps());
        }
        log.error("业务引擎执行异常：{} {}",String.format(ex.getMessage(),ex.getProps()),ex.getCause());
        return ResponseInfo.failure(FLOW_AUDIT_ERROR, ex.getMessage(), ex.getProps());
    }

}
