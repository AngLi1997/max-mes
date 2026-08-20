package com.bmos.mes.service.weigh.centre2.requirement.service;

import com.bmos.mes.service.weigh.centre2.requirement.dto.*;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementGroupInfoVO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementGroupPageVO;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketWeighRecordVO;
import com.bmos.mybatis.page.CommonPage;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * 工单需求Service接口
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:55
 */
public interface ITicketRequirementGroupService {
    
    /**
     * 创建工单需求
     * @param createDTO 工单需求创建信息
     * @return 创建的工单需求ID
     */
    Long createRequirementGroup(TicketRequirementGroupDTO createDTO);

    /**
     * 修改工单需求
     * @param editDTO 工单需求修改信息
     * @return 是否成功
     */
    Boolean editRequirementGroup(TicketRequirementGroupEditDTO editDTO);

    /**
     * 查询工单需求信息
     * @param query 查询参数
     * @return 工单需求信息
     */
    TicketRequirementGroupInfoVO queryInfo(@Valid TicketRequirementInfoQuery query);
    
    /**
     * 分页查询工单需求组
     * @param pageDTO 分页查询参数
     * @return 分页数据
     */
    CommonPage<TicketRequirementGroupPageVO> page(TicketRequirementGroupPageDTO pageDTO);
    
    /**
     * 确认称量工单需求
     * @param id 需求ID
     * @return 是否成功
     */
    Boolean makeSureRequirementGroup(Long id);
    
    /**
     * 取消称量工单需求
     * @param id 需求ID
     * @return 是否成功
     */
    Boolean cancelRequirement(Long id);

    void saveRequirement(TicketRequirementGroupRequirementDTO createDTO);

    BigDecimal calcFormulaQuantity(TicketCalcFormulaQuantityDTO dto);

    List<TicketWeighRecordVO> getWeighRecord(Long groupId);

    List<String> validateSaveRequirement(TicketRequirementGroupRequirementDTO createDTO);
}