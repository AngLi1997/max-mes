package com.bmos.platform.service.system.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 移动端密码修改
 */
@Getter
@Setter
@ApiModel("用户管理界面对用户进行密码修改DTO")
public class MobileChangePwdDTO {

    @ApiModelProperty(value = "新密码",required = true)
    @NotEmpty
    private String password;


}
