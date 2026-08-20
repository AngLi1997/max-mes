package com.bmos.mes.service.weigh.centre2.requirement.service;

import com.bmos.mes.service.weigh.centre2.requirement.dto.RequirementQueryDTO;
import com.bmos.mes.service.weigh.centre2.requirement.dto.TicketRequirementQueryDTO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementVO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.WeighRequirementListVO;

import java.util.List;

/**
 * 配料信息物料查询Service接口
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:16
 */
public interface ITicketRequirementService {

    /**
     * 根据物料ID查询配料信息物料列表
     * @param queryDTO 查询参数
     * @return 配料信息物料列表
     */
    List<TicketRequirementVO> queryMaterialList(TicketRequirementQueryDTO queryDTO);

    /**
     * 查询称量需求列表
     *
     * @param queryDTO 查询参数
     * @return 需求列表
     */
    List<WeighRequirementListVO> list(RequirementQueryDTO queryDTO);
} 