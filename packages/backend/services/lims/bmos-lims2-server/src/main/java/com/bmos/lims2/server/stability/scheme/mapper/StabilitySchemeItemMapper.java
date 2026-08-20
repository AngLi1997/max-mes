package com.bmos.lims2.server.stability.scheme.mapper;

import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeItem;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StabilitySchemeItemMapper extends BaseMapperX<StabilitySchemeItem> {

    List<StabilitySchemeItem> selectByVersionId(@Param("versionId") Long versionId);

    List<StabilitySchemeItemDTO> selectByVersionIdWithNames(@Param("versionId") Long versionId);

    void deleteByVersionId(@Param("versionId") Long versionId);
}
