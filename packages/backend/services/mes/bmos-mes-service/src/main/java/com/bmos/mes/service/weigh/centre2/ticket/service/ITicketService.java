package com.bmos.mes.service.weigh.centre2.ticket.service;

import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementDO;
import com.bmos.mes.service.weigh.centre2.ticket.dto.TicketEditDTO;
import com.bmos.mes.service.weigh.centre2.ticket.dto.TicketPageQuery;
import com.bmos.mes.service.weigh.centre2.ticket.entity.TicketDO;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketPageVO;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketWeighRecordVO;
import com.bmos.mybatis.page.CommonPage;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 工单Service接口
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:25
 */
public interface ITicketService {

    void programAuto();

    void programManual(@NotEmpty List<Long> requirementIds);

    CommonPage<TicketPageVO> page(TicketPageQuery pageDTO);
    
    /**
     * 下发工单
     * @param id 工单ID
     */
    void issue(@NotNull Long id);
    
    /**
     * 取消工单
     * @param id 工单ID
     */
    void cancel(@NotNull Long id);

    /**
     * 编辑工单
     * @param editDTO 工单编辑参数
     */
    void edit(@NotNull TicketEditDTO editDTO);

    /**
     * 获取工单信息
     * @param ticketId
     * @return
     */
    TicketDO getTicketInfo(Long ticketId);

    List<TicketWeighRecordVO> getWeighRecord(Long ticketId);

    List<TicketWeighRecordVO> getTicketWeighRecords(List<TicketRequirementDO> requirements);
}