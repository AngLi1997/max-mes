package com.bmos.mes.service.plan.team.service;

import com.bmos.mes.service.plan.team.dto.InstructionBatchConfirmDTO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamConfirmDTO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamProductStartConfirmDTO;
import com.bmos.mes.service.plan.team.model.InstructionTeam;
import com.bmos.mes.service.plan.team.vo.InstructionTeamDetailVO;
import com.bmos.mes.service.plan.team.vo.ProcedureStepChangeVO;
import com.bmos.mes.service.process.dto.ProcedureStepGroupUserDTO;
import com.bmos.mes.service.workflow.change.vo.TeamListVO;

import java.util.List;
import java.util.Map;

public interface InstructionTeamService {
    /**
     * 指令单详情
     *
     * @param id id
     * @return InstructionTeamDetailVO
     */
    InstructionTeamDetailVO detail(Long id);

    /**
     * 指令单确认
     *
     * @param dto dto
     */
    void confirm(InstructionTeamConfirmDTO dto);

    /**
     * 指令单保存
     *
     * @param dto dto
     */
    void save(InstructionTeamConfirmDTO dto);

    /**
     * 生产前确认
     *
     * @param dto dto
     */
    void startConfirm(InstructionTeamProductStartConfirmDTO dto);

    List<Long> getTeamIds(Long productPlanId, String nodeStepId);

    List<String> findInstructionPeople(ProcedureStepGroupUserDTO dto);

    /**
     * 指令单批量确认
     * @param dto
     */
    void batchConfirm(InstructionBatchConfirmDTO dto);

    /**
     * 创建任务查询班组信息
     * @param procedureNumber 工艺换班/工序换班数量
     * @param planId 计划id
     * @param nodeStepId 步骤id
     * @param changeTeamId 第一次换班班组信息
     * @param changeType 换班类型
     * @return
     */
    List<Long> getChangeTeamIds(Integer procedureNumber, Long planId, String nodeStepId, Map<Long,List<Long>> changeTeamId,String changeType);

    List<InstructionTeam> queryByPlanId(List<Long> planId);

    List<TeamListVO> getHistoryChangeTeam(Long planId,List<Long> teamIdS);

    /**
     * 根据生产计划id和工步id查询指令单列表
     * @param productPlanId
     * @param stepIds
     * @return
     */
    List<ProcedureStepChangeVO> queryByPlanIdAndStepIds(Long productPlanId, List<Long> stepIds);

    List<InstructionTeam> getInstructionDetailByUserTeamId(List<Long> team);
}
