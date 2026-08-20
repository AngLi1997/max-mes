package com.bmos.lims2.web.inspect.team.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 通过方案版本与检验项目查询到的班组人员-响应VO
 * @Author: yigaohui
 * @Date: 2025/11/20 00:00
 */
@Getter
@Setter
@ApiModel("班组人员响应VO")
public class TeamUserRespVO {

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("班组ID")
    private Long teamId;

    @ApiModelProperty("班组名称")
    private String teamName;

    @ApiModelProperty("用户登录名称")
    private String loginName;
}


