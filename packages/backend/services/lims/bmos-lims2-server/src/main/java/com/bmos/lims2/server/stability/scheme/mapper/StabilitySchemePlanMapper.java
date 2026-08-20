package com.bmos.lims2.server.stability.scheme.mapper;

import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemePlan;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StabilitySchemePlanMapper extends BaseMapperX<StabilitySchemePlan> {

    List<StabilitySchemePlan> selectByVersionId(@Param("versionId") Long versionId);

    void deleteByVersionId(@Param("versionId") Long versionId);
}
