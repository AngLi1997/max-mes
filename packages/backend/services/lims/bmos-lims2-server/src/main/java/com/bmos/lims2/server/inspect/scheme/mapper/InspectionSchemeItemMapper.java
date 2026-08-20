package com.bmos.lims2.server.inspect.scheme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeItemDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验方案检验项目配置Mapper接口
 * 正确的业务层级：检验方案明细 → 检验项目配置 → 分析项配置
 *
 * @author yigaohui
 * @since 2025/01/21 10:30
 */
@Mapper
public interface InspectionSchemeItemMapper extends BaseMapper<InspectionSchemeItem> {

    /**
     * 根据方案明细ID查询检验项目配置列表
     *
     * @param versionId 方案明细ID
     * @return 检验项目配置列表
     */
    List<InspectionSchemeItem> listByDetailId(@Param("versionId") Long versionId);

    /**
     * 根据方案明细ID查询检验项目配置列表（包含分析项配置）
     *
     * @param versionId 方案明细ID
     * @return 检验项目配置列表
     */
    List<InspectionSchemeItemDTO> listWithAnalyzeItems(@Param("versionId") Long versionId);
}