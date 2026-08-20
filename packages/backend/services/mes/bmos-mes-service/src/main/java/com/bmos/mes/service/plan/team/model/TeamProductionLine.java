package com.bmos.mes.service.plan.team.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* 班组-产线关联表
*/
@Data
@TableName(value = "bm_team_production_line")
public class TeamProductionLine{

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 班组id
     */
    private Long teamId;

    /**
     * 产线id
     */
    private Long productionLineId;


}
