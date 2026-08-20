package com.bmos.lims2.server.inspect.team.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 检验班组用户绑定
 */
@Data
@TableName("lm_inspection_team_user")
public class InspectionTeamUser {

    /**
     * 检验班组id
     */
    private Long inspectionTeamId;

    /**
     * 用户id
     */
    private String userId;

}
