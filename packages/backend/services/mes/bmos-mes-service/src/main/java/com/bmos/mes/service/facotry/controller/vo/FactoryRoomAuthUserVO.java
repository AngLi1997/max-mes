package com.bmos.mes.service.facotry.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 房间清场人、QA人员VO
 */
@Getter
@Setter
@ApiModel("房间清场人、QA人员VO")
public class FactoryRoomAuthUserVO {

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String userName;

    /**
     * 登录名
     */
    @ApiModelProperty("登录名")
    private String loginName;

}
