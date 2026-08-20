package com.bmos.mes.service.weigh.centre2.requirement.mapper;

import com.bmos.mes.service.weigh.centre2.requirement.dto.TicketRequirementGroupPageDTO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementGroupDO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementGroupPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工单需求Mapper接口
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:40
 */
@Mapper
public interface ITicketRequirementGroupMapper extends BaseMapperX<TicketRequirementGroupDO> {

    List<TicketRequirementGroupPageVO> queryGroupPage(@Param("page") TicketRequirementGroupPageDTO pageDTO, @Param("weighCentreIds") List<Long> weighCentreIds);
}