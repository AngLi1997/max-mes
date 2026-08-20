package com.bmos.platform.service.system.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 签名密码配置检查结果vo
 * @author liang
 * @version 1.0.0
 * @date 2024/11/26 14:19
 */
@Data
@ApiModel("签名密码配置检查结果")
@AllArgsConstructor
@NoArgsConstructor
public class CheckSignaturePasswordConfigResultVO {

    @ApiModelProperty(value = "用户id", example = "1")
    private String userId;

    @ApiModelProperty(value = "是否配置了签名密码", example = "true")
    private Boolean result;
}
