package com.bmos.lims2.server.inspect.scheme.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bmos.lims2.server.eln.entry.dto.CalculateDataQueryDTO;
import com.bmos.lims2.server.eln.entry.vo.FieldConfigVO;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeParameterDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检验方案分析项配置Mapper接口
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Mapper
public interface InspectionSchemeParameterMapper extends BaseMapper<InspectionSchemeParameter> {

    /**
     * 根据检验项目配置ID获取分析项配置列表
     *
     * @param itemConfigId 检验项目配置ID
     * @return 分析项配置列表
     */
    List<InspectionSchemeParameterDTO> listByItemConfigId(@Param("itemConfigId") Long itemConfigId);

    /**
     * 根据分析项配置ID获取分析项配置详情
     *
     * @param parameterConfigId 分析项配置ID
     * @return 分析项配置详情
     */
    InspectionSchemeParameterDTO getByParameterConfigId(@Param("parameterConfigId") Long parameterConfigId);

    /**
     * 根据分析项ID与版本ID获取最终判定表达式
     *
     * @param parameterId 分析项ID
     * @param versionId   方案版本ID
     * @return 最终判定表达式（可能为null）
     */
    String getFinalExpressionByParameterAndVersion(@Param("parameterId") Long parameterId,
                                                   @Param("versionId") Long versionId);


    /**
     * 查询分析项关联的方法的字段配置
     * @param query 查询条件
     * @param fieldIds 字段ID列表
     * @return 查询结果
     */
    List<FieldConfigVO> getFieldsConfig(@Param("query") CalculateDataQueryDTO query, @Param("fieldIds") List<Long> fieldIds);

    default List<InspectionSchemeParameter> selectByVersionId(Long versionId) {
        return selectList(new LambdaQueryWrapper<InspectionSchemeParameter>()
                .eq(InspectionSchemeParameter::getVersionId, versionId));
    }
}