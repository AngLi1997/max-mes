package com.bmos.platform.service.log.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.bmos.platform.service.log.convert.LoginActionStateConverter;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("登录日志VO")
public class LoginLogExcelVO {
    /**
     * 登录账号
     */
    @ExcelProperty("用户账户")
    private String loginName;

    /**
     * 用户名
     */
    @ExcelProperty("用户名称")
    private String userName;

    /**
     * ip
     */
    @ExcelProperty("IP")
    private String ip;

    /**
     * 操作动作
     */
    @ExcelProperty("操作动作")
    private String operationAction;

    /**
     * 操作状态
     */
    @ExcelProperty(value = "操作状态",converter = LoginActionStateConverter.class)
    private Boolean operationState;

    /**
     * 操作时间
     */
    @ExcelProperty("操作时间")
    private LocalDateTime createTime;

    /**
     * 描述代码
     */
    @ExcelIgnore
    private Integer descriptionCode;

    /**
     * 操作描述
     */
    @ExcelProperty("操作描述")
    private String description;

}
