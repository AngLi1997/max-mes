package com.bmos.mes.service.plan.instruction.service;

import com.bmos.mes.service.audit.vo.AuditCategoryCountVO;
import com.bmos.mes.service.plan.info.dto.PlanPageDTO;
import com.bmos.mes.service.plan.info.vo.PlanPageVO;
import com.bmos.mes.service.plan.instruction.dto.InstructionSaveDTO;
import com.bmos.mes.service.plan.instruction.dto.InstructionUpdateDTO;
import com.bmos.mes.service.plan.instruction.dto.TeamDetailQueryDTO;
import com.bmos.mes.service.plan.instruction.vo.InstructionDetailVO;
import com.bmos.mes.service.plan.instruction.vo.InstructionPageVO;
import com.bmos.mes.service.plan.instruction.vo.InstructionProcedureVO;
import com.bmos.mes.service.plan.instruction.vo.InstructionTeamVO;

import java.util.List;

public interface InstructionService {
    /**
     * 分页查询
     * @param dto dto
     * @return List<InstructionPageVO>
     */
    List<InstructionPageVO> page(PlanPageDTO dto);

    Integer waitTaskCount(String userId);

    /**
     * 分页查询
     * @param dto dto
     * @return List<InstructionPageVO>
     */
    List<PlanPageVO> startPage(PlanPageDTO dto);

    /**
     * 详情
     * @param id id
     * @return InstructionDetailVO
     */
    InstructionDetailVO detail(Long id);

    /**
     * 指令单分解保存
     * @param dto dto
     */
    Long save(InstructionSaveDTO dto);

    /**
     * 指令单分解更新
     * @param dto dto
     */
    void update(InstructionUpdateDTO dto);

    /**
     * 指令单生成
     * @param id 生产计划id
     */
    void generate(Long id, boolean autoConfirm);

    /**
     * 指令单下发
     * @param id 生产计划id
     */
    void send(Long id);

    List<InstructionProcedureVO> teamDetail(TeamDetailQueryDTO dto);
}
