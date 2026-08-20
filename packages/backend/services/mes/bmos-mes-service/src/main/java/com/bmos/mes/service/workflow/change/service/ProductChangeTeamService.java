package com.bmos.mes.service.workflow.change.service;

import com.bmos.mes.service.plan.instruction.vo.InstructionTeamVO;
import com.bmos.mes.service.workflow.change.dto.ChangeTeamDTO;
import com.bmos.mes.service.workflow.change.dto.TeamListDTO;
import com.bmos.mes.service.workflow.change.model.ProductChangeTeam;

import java.util.List;

/**
 * @author renjinguang
 */
public interface ProductChangeTeamService {

    List<ProductChangeTeam> saveChangeTeam(List<TeamListDTO> changeTeamList, Integer changeTeamNumber,String nodeFunction);

    List<ProductChangeTeam> queryByPlanIdAndProcedureModelId(ChangeTeamDTO teamDTO);

    /**
     *
     * @param planId 计划id
     * @param nodeFunction 3==工序换班。4==工艺换班
     * @return
     */
    List<InstructionTeamVO> selectListByPlanId(Long planId,String nodeFunction,Integer changeNumber);

}
