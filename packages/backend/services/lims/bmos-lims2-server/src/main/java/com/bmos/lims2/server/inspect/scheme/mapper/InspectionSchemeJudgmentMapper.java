package com.bmos.lims2.server.inspect.scheme.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeJudgment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验方案判定配置Mapper接口
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Mapper
public interface InspectionSchemeJudgmentMapper extends BaseMapper<InspectionSchemeJudgment> {

    /**
     * 根据分析项配置ID获取判定配置列表
     *
     * @param parameterConfigId 分析项配置ID
     * @return 判定配置列表
     */
    List<InspectionSchemeJudgmentDTO> listByParameterConfigId(@Param("parameterConfigId") Long parameterConfigId);

    default void deleteByDataPointConfigId(Long dataPointConfigId){
        LambdaQueryWrapper<InspectionSchemeJudgment> eq = new QueryWrapper<InspectionSchemeJudgment>().lambda().eq(InspectionSchemeJudgment::getDataPointConfigId, dataPointConfigId);
        delete(eq);
    }
}