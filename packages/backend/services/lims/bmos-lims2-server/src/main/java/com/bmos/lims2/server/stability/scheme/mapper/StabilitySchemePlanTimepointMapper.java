package com.bmos.lims2.server.stability.scheme.mapper;

import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlanTimepoint;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StabilitySchemePlanTimepointMapper extends BaseMapperX<StabilitySchemePlanTimepoint> {

    List<StabilitySchemePlanTimepoint> selectByPlanId(@Param("planId") Long planId);

    void deleteByPlanId(@Param("planId") Long planId);

    void deleteByVersionId(@Param("versionId") Long versionId);
}
