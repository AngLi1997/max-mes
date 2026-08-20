package com.bmos.lims2.common.model.execute;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class ExceptionDescriptionParam {

    /**
     * 值
     */
    private String value;

    /**
     * 原值
     */
    private String originalValue;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 复核名
     */
    private String reviewerName;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

}
