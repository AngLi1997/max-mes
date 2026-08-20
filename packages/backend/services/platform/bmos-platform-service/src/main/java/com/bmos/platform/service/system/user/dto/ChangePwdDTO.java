package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户管理界面对用户进行密码修改
 */
@Getter
@Setter
@ApiModel("用户管理界面对用户进行密码修改DTO")
public class ChangePwdDTO extends ChangeLoginPwdDTO{

    /**
     * 用户id
     */
    @ApiModelProperty("当前用户id")
    private Long id;

}
