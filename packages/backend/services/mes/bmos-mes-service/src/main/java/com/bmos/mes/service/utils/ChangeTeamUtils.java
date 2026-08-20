package com.bmos.mes.service.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.plan.team.service.InstructionTeamService;
import com.bmos.mes.service.plan.team.service.ProductPlanTeamService;
import com.bmos.mes.service.workflow.change.vo.TeamListVO;


import java.util.*;
import java.util.*;

public class ChangeTeamUtils {

    private static ProductPlanTeamService productPlanTeamService;

    private static InstructionTeamService instructionTeamService;

    public static void init(ProductPlanTeamService productPlanTeamService,InstructionTeamService instructionTeamService){
        ChangeTeamUtils.productPlanTeamService = productPlanTeamService;
        ChangeTeamUtils.instructionTeamService = instructionTeamService;
    }

    /**
     * 获取当前登录人最新班次信息
     * @param planId
     * @return
     */
    public static List<TeamListVO> getTeam(List<Long> planId,String userId) {
        if (CollUtil.isEmpty(planId)){
            return Collections.emptyList();
        }
        //当前登录用户班组id
        List<Long> team = productPlanTeamService.getListByUserId(userId);
        //获取当前批次的班组信息
        List<InstructionTeam> instructionTeams = instructionTeamService.queryByPlanId(planId);
        List<TeamListVO> teamList = new ArrayList<>();
        instructionTeams.forEach(item->{
            //当前登录用户班组在班组集合当中
            if (CollUtil.isNotEmpty(CollectionUtils.filterList(item.getTeamIds(), team::contains))) {
                teamList.add(BeanUtil.toBean(item,TeamListVO.class));
            }
        });
        return teamList;
    }

    /**
     * 根据当前用户获取所有换班次数
     * @return
     * @param planId 计划id
     */
    public static List<TeamListVO> getHistoryChangeTeam(Long planId){
        //当前登录用户班组id
        List<Long> team = productPlanTeamService.getListByUserId(SysUserHolder.getUser().getUserId());
        if (CollUtil.isEmpty(team)){
            return Collections.emptyList();
        }
        return instructionTeamService.getHistoryChangeTeam(planId,team);
    }

    /**
     * 根据当前登录人班组获取所有计划信息
     */
    public static List<InstructionTeam> getInstructionDetailByUserTeamId(){
        List<Long> team = productPlanTeamService.getListByUserId(SysUserHolder.getUser().getUserId());
        if (CollUtil.isEmpty(team)){
            return new LinkedList<>();
        }
        return instructionTeamService.getInstructionDetailByUserTeamId(team);
    }


}
