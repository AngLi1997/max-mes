package com.bmos.mes.service.weigh.centre2.ticket.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.centre2.execute.controller.vo.WeighTicketPageVO;
import com.bmos.mes.service.weigh.centre2.execute.service.dto.WeighTicketPageDTO;
import com.bmos.mes.service.weigh.centre2.ticket.dto.TicketPageQuery;
import com.bmos.mes.service.weigh.centre2.ticket.entity.TicketDO;
import com.bmos.mes.service.weigh.centre2.ticket.vo.TicketPageVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 工单Mapper接口
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:20
 */
@Mapper
public interface ITicketMapper extends BaseMapperX<TicketDO> {

    /**
     * 根据工单编号查询工单
     * @param ticketNo 工单编号
     * @return 工单列表
     */
    default List<TicketDO> listByTicketNo(String ticketNo) {
        if (StringUtils.isBlank(ticketNo)) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<TicketDO>()
                .eq(TicketDO::getTicketNo, ticketNo)
        );
    }

    /**
     * 根据称量中心ID查询工单
     * @param weighCentreId 称量中心ID
     * @return 工单列表
     */
    default List<TicketDO> listByWeighCentreId(Long weighCentreId) {
        return selectList(new LambdaQueryWrapper<TicketDO>()
                .eq(TicketDO::getWeighCentreId, weighCentreId)
        );
    }

    /**
     * 根据计划日期查询工单
     * @param planDate 计划日期
     * @return 工单列表
     */
    default List<TicketDO> listByPlanDate(LocalDate planDate) {
        return selectList(new LambdaQueryWrapper<TicketDO>()
                .eq(TicketDO::getPlanDate, planDate)
        );
    }

    List<TicketPageVO> queryPage(@Param("page") TicketPageQuery pageDTO, @Param("weighCentreIds") List<Long> weighCentreIds);

    /**
     * 分页条件查询工单
     */
    List<WeighTicketPageVO> selectPageByCondition(@Param("dto") WeighTicketPageDTO dto, @Param("history") boolean history, @Param("weighCentreIds") List<Long> weighCentreIds);
}