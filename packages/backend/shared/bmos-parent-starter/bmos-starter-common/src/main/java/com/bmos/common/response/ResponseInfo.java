package com.bmos.common.response;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.util.i18n.I18nUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ToString
public class ResponseInfo<T> {

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", required = true)
    private int code;
    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息", required = true)
    private String message;

    /**
     * 数据内容
     */
    @ApiModelProperty(value = "数据内容", required = true)
    private T data;

    @ApiModelProperty(value = "响应参数", hidden = true)
    private Object[] args;

    public ResponseInfo() {
    }

    public ResponseInfo(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseInfo(int code, String message, T data, Object[] args) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.args = args;
    }

    public static <T> ResponseInfo<T> success(T data) {
        return new ResponseInfo<>(BaseResponseCode.SUCCESS.getCode(), BaseResponseCode.SUCCESS.getMessage(), data);
    }

    public static <T> ResponseInfo<T> success() {
        return new ResponseInfo<>(BaseResponseCode.SUCCESS.getCode(), BaseResponseCode.SUCCESS.getMessage(), null);
    }


    public static <T> ResponseInfo<T> failure(int code, String message) {
        return new ResponseInfo<>(code, message, null);
    }

    public static <T> ResponseInfo<T> failure(int code, String message, Object[]... args) {
        return new ResponseInfo<>(code, message, null, args);
    }

    public static <T> ResponseInfo<T> failure(ResponseItem item) {
        return new ResponseInfo<>(item.getCode(), item.getMessage(), null);
    }

    public static <T> ResponseInfo<T> failure(ResponseItem item, Object... args) {
        return new ResponseInfo<>(item.getCode(), item.getMessage(), null, args);
    }

    public static <T> ResponseInfo<T> failure(ResponseItem item, T data) {
        return new ResponseInfo<>(item.getCode(), item.getMessage(), data);
    }

    public static <T> ResponseInfo<T> failure(ResponseItem item, String errorDetail) {
        if (StrUtil.isBlank(errorDetail)) {
            return new ResponseInfo<>(item.getCode(), item.getMessage(), null);
        }
        return new ResponseInfo<>(item.getCode(), String.format("%s[%s]", item.getMessage(), errorDetail), null);
    }

    public static <T> ResponseInfo<T> failure(ResponseItem item, String errorDetail, T data) {
        if (StrUtil.isBlank(errorDetail)) {
            return new ResponseInfo<>(item.getCode(), item.getMessage(), null);
        }
        return new ResponseInfo<>(item.getCode(), String.format("%s[%s]", item.getMessage(), errorDetail), data);
    }

    public int getCode() {
        return code;
    }

    /**
     * jackson序列化会自动调用getMessage方法
     * 通过对getMessage方法改造对message进行国际化
     * @return
     */
    public String getMessage() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNull(requestAttributes)) {
            return message;
        }
        return I18nUtils.getResponseMessage(code, message, args, requestAttributes.getRequest());
    }


    public T getData() {
        return data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return BaseResponseCode.SUCCESS.getCode() == getCode();
    }

    @JsonIgnore
    public boolean isError() {
        return !isSuccess();
    }


}
