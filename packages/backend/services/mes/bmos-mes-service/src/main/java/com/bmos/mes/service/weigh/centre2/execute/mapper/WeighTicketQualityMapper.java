package com.bmos.mes.service.weigh.centre2.execute.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighTicketQualityDO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;

@Mapper
public interface WeighTicketQualityMapper extends BaseMapperX<WeighTicketQualityDO> {
   /**
     * 根据称量工单ID查询质量DO
     */
    default WeighTicketQualityDO selectByTicketId(Long weighTicketId) {
        QueryWrapper<WeighTicketQualityDO> wrapper = new QueryWrapper<>();
        wrapper.eq("weigh_ticket_id", weighTicketId);
        return this.selectOne(wrapper);
    }

    /**
     * 根据工单ID更新称量质量
     * @param weighTicketId 工单ID
     * @param weighQulity 称量质量
     * @return 更新条数
     */
    default void updateWeighQuality(Long weighTicketId, BigDecimal weighQulity) {
        UpdateWrapper<WeighTicketQualityDO> wrapper = new UpdateWrapper<>();
        wrapper.eq("weigh_ticket_id", weighTicketId)
               .set("weigh_quality", weighQulity);
        this.update(null, wrapper);
    }
} 