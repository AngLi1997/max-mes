package com.bmos.lims2.server.inspect.scheme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeSamplingDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeSampling;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验方案取样量配置Mapper接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Mapper
public interface InspectionSchemeSamplingMapper extends BaseMapper<InspectionSchemeSampling> {

    List<InspectionSchemeSamplingDTO> listBySchemeVersion(@Param("schemeId") Long schemeId, @Param("versionId") Long versionId);

    List<InspectionSchemeSamplingDTO> selectBySchemeVersion(@Param("schemeId") Long schemeId, @Param("versionId") Long versionId);
}