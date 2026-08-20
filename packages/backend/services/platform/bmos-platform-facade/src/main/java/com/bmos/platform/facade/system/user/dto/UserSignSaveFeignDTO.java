package com.bmos.platform.facade.system.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 用户手写签名保存dto
 */
@Getter
@Setter
public class UserSignSaveFeignDTO {


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

    /**
     * 用户id
     */
    @NotEmpty
    private String userId;
}
