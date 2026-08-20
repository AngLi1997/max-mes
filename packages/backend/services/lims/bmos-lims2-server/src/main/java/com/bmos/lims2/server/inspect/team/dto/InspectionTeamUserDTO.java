package com.bmos.lims2.server.inspect.team.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 按方案版本与检验项目查询到的班组人员DTO
 * @Author: yigaohui
 * @Date: 2025/11/20 00:00
 */
@Getter
@Setter
@ApiModel("班组人员DTO")
public class InspectionTeamUserDTO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private String userId;

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String userName;

    /**
     * 班组ID
     */
    @ApiModelProperty("班组ID")
    private Long teamId;

    /**
     * 班组名称
     */
    @ApiModelProperty("班组名称")
    private String teamName;

    @ApiModelProperty("登录名")
    private String loginName;

    public String getUserName() {
        return UserUtils.getUsername(userId);
    }

    public String getLoginName() {
        return UserUtils.getUser(userId) == null ? null : UserUtils.getUser(userId).getLoginName();
    }
}


