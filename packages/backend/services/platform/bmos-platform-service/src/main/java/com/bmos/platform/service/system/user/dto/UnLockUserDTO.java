package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 解锁账户
 */
@Data
@ApiModel("解锁账户DTO")
public class UnLockUserDTO {

    /**
     * 用户id
     */
    private String userId;

}
