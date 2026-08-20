package com.bmos.lims2.server.inspect.scheme.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeItemTeams;
import org.apache.ibatis.annotations.Mapper;

/**
 * 检验方案分析项配置Mapper接口
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Mapper
public interface InspectionSchemeItemTeamMapper extends BaseMapper<InspectionSchemeItemTeams> {
}