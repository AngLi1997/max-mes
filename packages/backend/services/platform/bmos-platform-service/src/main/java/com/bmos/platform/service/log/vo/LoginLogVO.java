package com.bmos.platform.service.log.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("登录日志VO")
public class LoginLogVO {
    /**
     * id
     */
    private Long id;

    /**
     * 登录账号
     */
    private String loginName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 描述代码
     */
    private Integer descriptionCode;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作状态
     */
    private Boolean operationState;

    /**
     * 操作动作
     */
    private String operationAction;

    /**
     * ip
     */
    private String ip;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 描述参数
     */
    private String descriptionParam;

}
