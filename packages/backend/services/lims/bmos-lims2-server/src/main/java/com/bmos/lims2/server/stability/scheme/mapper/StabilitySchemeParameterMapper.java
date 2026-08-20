package com.bmos.lims2.server.stability.scheme.mapper;

import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO;
import com.bmos.lims2.server.stability.scheme.entity.StabilitySchemeParameter;
import com.bmos.lims2.server.inspect.entry.dto.HeaderGroupRowDTO;
import com.bmos.lims2.server.eln.entry.dto.CalculateDataQueryDTO;
import com.bmos.lims2.server.eln.entry.vo.FieldConfigVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StabilitySchemeParameterMapper extends BaseMapperX<StabilitySchemeParameter> {

    List<StabilitySchemeParameter> selectByItemId(@Param("itemId") Long itemId);

    List<StabilitySchemeParameter> selectByVersionId(@Param("versionId") Long versionId);

    List<StabilitySchemeParameterDTO> selectByVersionIdWithNames(@Param("versionId") Long versionId);

    List<StabilitySchemeParameterDTO> selectByItemIdWithNames(@Param("itemId") Long itemId);

    void deleteByVersionId(@Param("versionId") Long versionId);

    void deleteByItemId(@Param("itemId") Long itemId);

    /**
     * 按稳定性方案ID查询表头分组（分析项-数据点），跨版本合并去重，供 ELN 表头加载使用
     */
    List<HeaderGroupRowDTO> listHeaderGroupsByStabilityScheme(@Param("schemeId") Long schemeId);

    /**
     * 按分析项ID和方案版本ID查询最终判定表达式（供判定服务稳定性分支使用）
     */
    String getFinalExpressionByParameterAndVersion(@Param("parameterId") Long parameterId,
                                                   @Param("versionId") Long versionId);

    /**
     * 根据参数配置ID查询参数配置信息（含标准规定）
     */
    com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO getByParameterConfigId(@Param("parameterConfigId") Long parameterConfigId);

    /**
     * 查询稳定性方案下的字段配置（供ELN公式计算使用，镜像常规检验 getFieldsConfig）
     */
    List<FieldConfigVO> getFieldsConfig(@Param("query") CalculateDataQueryDTO query, @Param("fieldIds") List<Long> fieldIds);
}
