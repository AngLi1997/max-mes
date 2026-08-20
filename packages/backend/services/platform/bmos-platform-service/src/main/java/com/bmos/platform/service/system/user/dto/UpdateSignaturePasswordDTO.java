package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 更新签名密码dto
 * @author liang
 * @version 1.0.0
 * @date 2024/11/26 14:00
 */
@Data
@ApiModel("更新签名密码dto")
public class UpdateSignaturePasswordDTO {

    @NotBlank
    @ApiModelProperty(value = "登陆密码", example = "1")
    private String loginPassword;

    @NotBlank
    @ApiModelProperty(value = "签名密码", example = "1")
    private String signaturePassword;
}
