package com.bmos.lims2.server.stability.sample.mapper;

import com.bmos.lims2.server.stability.sample.entity.StabilitySampleMoveLog;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性样品移动记录Mapper
 */
@Mapper
public interface StabilitySampleMoveLogMapper extends BaseMapperX<StabilitySampleMoveLog> {

    /**
     * 查询指定样品的移动记录列表（按移动时间升序）
     */
    default List<StabilitySampleMoveLog> selectByPlanSampleId(Long planSampleId) {
        return selectList(new com.bmos.mybatis.query.LambdaQueryWrapperX<StabilitySampleMoveLog>()
                .eq(StabilitySampleMoveLog::getPlanSampleId, planSampleId)
                .orderByAsc(StabilitySampleMoveLog::getMoveTime));
    }
}
