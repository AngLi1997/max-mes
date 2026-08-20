package com.bmos.lims2.server.inspect.scheme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 检验方案版本Mapper接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Mapper
public interface InspectionSchemeVersionMapper extends BaseMapper<InspectionSchemeVersion> {

    /**
     * 检查版本号是否重复
     *
     * @param schemeId 方案ID
     * @param versionNo 版本号
     * @param excludeId 排除的ID
     * @return 重复数量
     */
    Integer checkVersionNoDuplicate(@Param("schemeId") Long schemeId, @Param("versionNo") String versionNo, @Param("excludeId") Long excludeId);

    /**
     * 获取当前生效的版本
     *
     * @param schemeId 方案ID
     * @return 生效的版本
     */
    InspectionSchemeVersion getActiveVersion(@Param("schemeId") Long schemeId);

    /**
     * 停用所有版本（除了指定的版本）
     *
     * @param schemeId 方案ID
     * @param excludeId 排除的版本ID
     * @param updateBy 更新人ID
     */
    void deactivateAllVersions(@Param("schemeId") Long schemeId, @Param("excludeId") Long excludeId, @Param("updateBy") Long updateBy);

    /**
     * 根据方案ID查询版本列表（用于下拉）
     */
    List<com.bmos.lims2.server.inspect.scheme.dto.response.SchemeVersionOptionDTO> listBySchemeId(@Param("schemeId") Long schemeId);

    /**
     * 查询方案版本关联的操作规程列表（去重），取每条操作规程的最新版本
     */
    List<com.bmos.lims2.server.inspect.scheme.dto.response.SchemeVersionOperateRuleDTO> listOperateRulesByVersionId(@Param("schemeVersionId") Long schemeVersionId);

} 