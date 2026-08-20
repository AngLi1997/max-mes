package com.bmos.web.exception;


import cn.hutool.core.util.ArrayUtil;
import com.bmos.common.exception.ActiveException;
import com.bmos.common.exception.AuthorizationException;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bmos.common.exception.BaseResponseCode.DUPLICATE_KEY_ERROR;
import static com.bmos.common.exception.BaseResponseCode.ILLEGAL_REQUEST_PARAMETER;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private  final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 业务异常处理
     *
     * @param ex      业务异常
     * @param request 请求
     * @return 返回值
     */
    @ExceptionHandler({Exception.class})
    public ResponseInfo<?> exceptionHandler(Exception ex, HttpServletRequest request) {
        log.error("业务异常：{} {} {}", request.getMethod(), request.getRequestURI(), ExceptionUtils.getStackTrace(ex));
        return ResponseInfo.failure(BaseResponseCode.SERVER_EXCEPTION);
    }

    @ExceptionHandler(value = BmosException.class)
    public ResponseInfo<?> bizExceptionHandler(BmosException ex, HttpServletRequest request) {
        log.error("业务异常：{} {} {}", request.getMethod(), request.getRequestURI(), ExceptionUtils.getStackTrace(ex));
        return ResponseInfo.failure(ex.getResponseItem(), ex.getResponseItem().getArgs());
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseInfo<?> validationExceptionHandler(ValidationException ex, HttpServletRequest request) {
        log.error("参数校验异常：{} {} {}", request.getMethod(), request.getRequestURI(), ExceptionUtils.getStackTrace(ex));
        return ResponseInfo.failure(ILLEGAL_REQUEST_PARAMETER, ex.getMessage());
    }

    @ExceptionHandler({AuthorizationException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseInfo<?> authorizationException(AuthorizationException ex) {
        return ResponseInfo.failure(ex.getResponseItem());
    }

    @ExceptionHandler({ActiveException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseInfo<?> activeException(ActiveException ex) {
        return ResponseInfo.failure(ex.getResponseItem());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseInfo<Map<String, String>> methodArgumentNotValidHandler(MethodArgumentNotValidException be) {
        return ResponseInfo.failure(ILLEGAL_REQUEST_PARAMETER, this.fromFieldErrors(be.getFieldErrors()));
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseInfo<Map<String, String>> constraintViolationExceptionHandler(ConstraintViolationException e, HttpServletRequest request) {

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        Map<String, String> result = new HashMap<>(violations.size());
        for (ConstraintViolation<?> constraintViolation : violations) {
            String propertyName = ((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName();
            String annotationName = constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
            result.put(propertyName, this.messageSource.getMessage(annotationName, null, LocaleContextHolder.getLocale()));
        }

        log.debug("入参校验未通过：{} {}，{}", request.getMethod(), request.getRequestURI(), result);
        return ResponseInfo.failure(ILLEGAL_REQUEST_PARAMETER, result);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseInfo<Void> requestIllegalArgumentException(IllegalArgumentException ex){
        log.error("参数异常：{}", ExceptionUtils.getStackTrace(ex));
        return ResponseInfo.failure(BaseResponseCode.ILLEGAL_REQUEST_PARAMETER);
    }

    //todo 其余异常

    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseInfo<Map<String, String>> bindExceptionHandler(BindException e, HttpServletRequest request) {
        return ResponseInfo.failure(ILLEGAL_REQUEST_PARAMETER, this.fromFieldErrors(e.getBindingResult().getFieldErrors()));
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseInfo<?> doError(DuplicateKeyException e) {
        return ResponseInfo.failure(DUPLICATE_KEY_ERROR);
    }
    private Map<String, String> fromFieldErrors(List<FieldError> fieldErrors) {
        Map<String, String> result = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            String[] codes = fieldError.getCodes();
            if (ArrayUtil.isEmpty(codes)) {
                continue;
            }
            String msg;
            try {
                msg = this.messageSource.getMessage(codes[codes.length - 1], null, LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException e) {
                log.warn("未找到国际化提示信息:{}", codes[codes.length - 1]);
                msg = fieldError.getDefaultMessage();
            }
            result.put(fieldError.getField(), msg);
        }
        return result;
    }


}
