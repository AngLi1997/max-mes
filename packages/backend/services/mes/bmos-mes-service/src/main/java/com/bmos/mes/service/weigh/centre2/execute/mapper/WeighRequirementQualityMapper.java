package com.bmos.mes.service.weigh.centre2.execute.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bmos.audit.engine.core.db.repository.base.LambdaQueryWrapperX;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighRequirementQualityDO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface WeighRequirementQualityMapper extends BaseMapperX<WeighRequirementQualityDO> {
    
    default WeighRequirementQualityDO selectByRequirementId(Long weighTicketRequirementId){
        return selectOne(new LambdaQueryWrapperX<WeighRequirementQualityDO>()
                .eq(WeighRequirementQualityDO::getWeighTicketRequirementId, weighTicketRequirementId)
                .last(" limit 1"));
    }


    default void updateWeighQuality(Long weighTicketRequirementId, BigDecimal weighQuality){
        WeighRequirementQualityDO weighRequirementQualityDO = new WeighRequirementQualityDO();
        weighRequirementQualityDO.setWeighQuality(weighQuality);
        update(weighRequirementQualityDO, new LambdaUpdateWrapper<WeighRequirementQualityDO>()
                        .eq(WeighRequirementQualityDO::getWeighTicketRequirementId, weighTicketRequirementId));
    }

    default List<WeighRequirementQualityDO> selectByRequirementIdList(List<Long> requirementIds){
        return selectList(new LambdaQueryWrapperX<WeighRequirementQualityDO>().in(WeighRequirementQualityDO::getWeighTicketRequirementId, requirementIds));
    }
}