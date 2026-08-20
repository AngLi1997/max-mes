package com.bmos.lims2.server.stability.plan.mapper;

import com.bmos.lims2.server.stability.plan.entity.StabilityInspectPlan;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性考察计划Mapper接口
 */
@Mapper
public interface StabilityInspectPlanMapper extends BaseMapperX<StabilityInspectPlan> {

    /**
     * 根据编号查询计划
     */
    StabilityInspectPlan selectByCode(@Param("code") String code);

    /**
     * 获取当前最大序号（用于生成计划编号）
     */
    Long selectMaxSeq();

}
