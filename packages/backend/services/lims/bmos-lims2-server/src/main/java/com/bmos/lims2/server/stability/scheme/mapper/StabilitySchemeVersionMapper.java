package com.bmos.lims2.server.stability.scheme.mapper;

import com.bmos.lims2.common.enums.StabilitySchemeVersionStatusEnum;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeVersion;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 稳定性方案版本Mapper接口
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@Mapper
public interface StabilitySchemeVersionMapper extends BaseMapperX<StabilitySchemeVersion> {

    /**
     * 根据方案ID查询版本列表
     *
     * @param schemeId 方案ID
     * @return 版本列表
     */
    List<StabilitySchemeVersion> selectBySchemeId(@Param("schemeId") Long schemeId);

    /**
     * 根据方案ID和版本号查询版本
     *
     * @param schemeId 方案ID
     * @param versionNo 版本号
     * @return 版本信息
     */
    StabilitySchemeVersion selectBySchemeIdAndVersionNo(@Param("schemeId") Long schemeId,
                                                       @Param("versionNo") String versionNo);

    /**
     * 查询指定方案下指定状态的版本数量
     *
     * @param schemeId 方案ID
     * @param status 状态
     * @return 数量
     */
    int countBySchemeIdAndStatus(@Param("schemeId") Long schemeId,
                                 @Param("status") StabilitySchemeVersionStatusEnum status);

    /**
     * 获取指定方案的最新版本号
     *
     * @param schemeId 方案ID
     * @return 最新版本号
     */
    String selectLatestVersionNo(@Param("schemeId") Long schemeId);

    /**
     * 查询指定方案下所有生效版本（允许多个并存）
     */
    List<StabilitySchemeVersion> selectActiveVersions(@Param("schemeId") Long schemeId);

}
