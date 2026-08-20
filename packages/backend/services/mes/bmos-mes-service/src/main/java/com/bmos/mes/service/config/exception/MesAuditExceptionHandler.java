package com.bmos.mes.service.config.exception;

import com.bmos.audit.engine.core.exception.InfiniteEngineException;
import com.bmos.audit.engine.core.exception.InfiniteGraphException;
import com.bmos.common.response.ResponseInfo;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.bmos.mes.common.exception.MesResponseCode.FLOW_AUDIT_ERROR;

@RestControllerAdvice
public class MesAuditExceptionHandler {

    @ExceptionHandler({InfiniteEngineException.class})
    public ResponseInfo<?> authorizationException(InfiniteEngineException ex) {
        return ResponseInfo.failure(FLOW_AUDIT_ERROR, ex.getMessage());
    }

    @ExceptionHandler({InfiniteGraphException.class})
    public ResponseInfo<?> authorizationGraphException(InfiniteGraphException ex) {
        return ResponseInfo.failure(FLOW_AUDIT_ERROR, ex.getMessage());
    }

}
