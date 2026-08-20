package com.bmos.mes.service.preparation.produce.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 确认配液产出时的复核人员VO
 */
@Data
@ApiModel("配液产出复核人员VO")
public class PreparationProduceUserVO {

    /**
     * 账户id
     */
    @ApiModelProperty(value = "账户id", example = "1")
    private String userId;

    /**
     * 登录账户名称
     */
    @ApiModelProperty(value = "登录账户名称", example = "1")
    private String loginName;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", example = "1")
    private String userName;

    /**
     * 前端展示名称
     */
    @ApiModelProperty(value = "前端展示名称", example = "zry-张若雨")
    private String showName;

}
