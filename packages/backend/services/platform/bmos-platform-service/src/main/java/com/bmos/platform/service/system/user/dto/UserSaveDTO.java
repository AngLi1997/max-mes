package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("用户保存DTO")
@Getter
@Setter
@ToString
public class UserSaveDTO{

    @ApiModelProperty("用户名称")
    @NotBlank
    @Length(max = 30)
    private String userName;

    @ApiModelProperty("用户账号")
    @NotBlank
    @Length(max = 30)
    private String loginName;

    @ApiModelProperty("性别")
    @NotNull
    private Integer gender;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("备注")
    private String remark;

}
