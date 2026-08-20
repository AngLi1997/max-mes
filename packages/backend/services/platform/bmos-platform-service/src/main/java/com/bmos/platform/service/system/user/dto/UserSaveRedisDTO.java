package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ApiModel("用户存入redisDTO")
@Getter
@Setter
@ToString
@Builder
public class UserSaveRedisDTO {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("终端类型 0代表PC端 1、2、3....代表App端")
    private Long type;

    @ApiModelProperty("账号")
    private String loginName;
}
