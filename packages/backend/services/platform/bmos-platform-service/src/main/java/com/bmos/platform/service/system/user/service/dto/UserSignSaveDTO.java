package com.bmos.platform.service.system.user.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 用户手写签名保存DTO
 */
@Getter
@Setter
@ApiModel("用户手写签名保存DTO")
public class UserSignSaveDTO {

    /**
     * Base64编码的文件内容
     */
    @NotEmpty
    private String fileBase64Content;

    /**
     * 文件后缀
     */
    @NotEmpty
    private String suffix;

}
