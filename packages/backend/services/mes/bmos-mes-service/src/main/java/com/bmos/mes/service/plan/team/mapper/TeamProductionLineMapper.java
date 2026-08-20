package com.bmos.mes.service.plan.team.mapper;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.plan.team.model.TeamProductionLine;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface TeamProductionLineMapper extends BaseMapperX<TeamProductionLine> {

    default void deleteByTeamId(Long id){
        delete(new LambdaQueryWrapperX<TeamProductionLine>()
                .eq(TeamProductionLine::getTeamId, id));
    }

    default List<TeamProductionLine> selectByTeamId(Long id){
        return selectList(new LambdaQueryWrapperX<TeamProductionLine>()
                .eq(TeamProductionLine::getTeamId, id));
    }

    default List<TeamProductionLine> selectByProductionLineIds(List<Long> productionLineIds){
        if (CollUtil.isEmpty(productionLineIds)) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<TeamProductionLine>()
                .in(TeamProductionLine::getProductionLineId, productionLineIds));
    }
}
