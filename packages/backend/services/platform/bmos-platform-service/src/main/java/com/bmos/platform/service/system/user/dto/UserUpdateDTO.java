package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotNull;

@ApiModel("用户更新DTO")
@Getter
@Setter
@ToString
@Builder
public class UserUpdateDTO {

    @Tolerate
    public UserUpdateDTO(){}

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户账号")
    private String loginName;

    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("解锁状态")
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;
}
