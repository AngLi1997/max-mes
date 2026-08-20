package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

/**
 * 更新签名密码dto
 * @author liang
 * @version 1.0.0
 * @date 2024/11/26 14:00
 */
@Data
@ApiModel("检查用户是否配置签名密码dto")
@NoArgsConstructor
@AllArgsConstructor
public class CheckSignaturePasswordConfigDTO {

    /**
     * 用户id
     */
    @NotEmpty
    @ApiModelProperty("用户id列表")
    private Collection<String> userIds;
}
