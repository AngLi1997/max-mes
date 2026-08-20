package com.bmos.mes.service.weigh.centre2.execute.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighTicketUserDO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WeighTicketUserMapper extends BaseMapperX<WeighTicketUserDO> {
    // 根据工单id查操作人
    default WeighTicketUserDO getByTicketId(Long ticketId) {
        return selectOne(new LambdaQueryWrapper<WeighTicketUserDO>()
                .eq(WeighTicketUserDO::getWeighTicketId, ticketId)
        );
    }
    // 绑定操作人
    default void bindOperator(Long ticketId, String userId, String signUser, String remark) {
        WeighTicketUserDO userDO = new WeighTicketUserDO();
        userDO.setWeighTicketId(ticketId);
        userDO.setOperator(userId);
        userDO.setSignUser(signUser);
        userDO.setRemark(remark);
        insert(userDO);
    }
    // 解绑操作人
    default void unbindOperator(Long ticketId) {
        delete(new LambdaQueryWrapper<WeighTicketUserDO>()
                .eq(WeighTicketUserDO::getWeighTicketId, ticketId)
        );
    }
    // 批量根据工单id查操作人
    default List<WeighTicketUserDO> getByTicketIds(List<Long> ticketIds) {
        if (ticketIds == null || ticketIds.isEmpty()) return java.util.Collections.emptyList();
        return selectList(new LambdaQueryWrapper<WeighTicketUserDO>()
                .in(WeighTicketUserDO::getWeighTicketId, ticketIds)
        );
    }
} 