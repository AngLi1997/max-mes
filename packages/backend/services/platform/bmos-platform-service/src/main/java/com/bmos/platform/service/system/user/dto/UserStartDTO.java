package com.bmos.platform.service.system.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel("用户启用DTO")
@Getter
@Setter
@ToString
public class UserStartDTO {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("用户id")
    @NotBlank
    private String userId;

    @ApiModelProperty("启用状态")
    @NotNull
    private Integer state;
}
