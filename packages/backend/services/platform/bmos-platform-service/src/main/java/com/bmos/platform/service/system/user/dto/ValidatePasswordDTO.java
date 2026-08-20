package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class ValidatePasswordDTO {

    @ApiModelProperty("密码")
    @NotBlank
    private String password;
}
