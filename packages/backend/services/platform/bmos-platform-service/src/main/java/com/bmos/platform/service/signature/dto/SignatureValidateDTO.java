package com.bmos.platform.service.signature.dto;

import com.bmos.platform.common.enums.signature.SignatureActionEnum;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("签名校验DTO")
public class SignatureValidateDTO {

    @ApiModelProperty("签名动作")
    @NotNull
    private SignatureActionEnum signatureAction;

    @ApiModelProperty(value = "登录名称",required = true)
    @NotEmpty
    private String loginName;

    @ApiModelProperty(value = "密码",required = true)
    @NotEmpty
    private String password;
}
