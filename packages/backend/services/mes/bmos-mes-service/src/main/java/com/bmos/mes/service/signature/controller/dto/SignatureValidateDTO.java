package com.bmos.mes.service.signature.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@ApiModel("签名校验DTO")
public class SignatureValidateDTO {

    @ApiModelProperty(value = "登录名称",required = true)
    @NotEmpty
    private String loginName;

    @ApiModelProperty(value = "密码",required = true)
    @NotEmpty
    private String password;

    @ApiModelProperty(value = "关联数据(保留字段，顺便传啥)",required = true)
    @NotEmpty
    private String signatureData;
}
