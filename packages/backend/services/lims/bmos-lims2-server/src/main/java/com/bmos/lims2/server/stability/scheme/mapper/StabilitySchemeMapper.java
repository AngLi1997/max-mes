package com.bmos.lims2.server.stability.scheme.mapper;

import com.bmos.lims2.server.stability.scheme.entity.StabilityScheme;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性方案Mapper接口
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Mapper
public interface StabilitySchemeMapper extends BaseMapperX<StabilityScheme> {

    /**
     * 根据条件查询稳定性方案列表
     *
     * @param name 方案名称（模糊查询）
     * @param code 方案编码（模糊查询）
     * @param materialId 检品ID
     * @return 方案列表
     */
    List<StabilityScheme> selectSchemeList(@Param("name") String name,
                                           @Param("code") String code,
                                           @Param("materialId") Long materialId);

    /**
     * 根据方案编码查询方案
     *
     * @param code 方案编码
     * @return 方案信息
     */
    StabilityScheme selectByCode(@Param("code") String code);

    /**
     * 根据检品ID查询方案
     *
     * @param materialId 检品ID
     * @return 方案信息
     */
    StabilityScheme selectByMaterialId(@Param("materialId") Long materialId);
}
