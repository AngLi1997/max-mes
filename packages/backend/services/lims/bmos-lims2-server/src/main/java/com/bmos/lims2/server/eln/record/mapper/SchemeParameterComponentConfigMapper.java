package com.bmos.lims2.server.eln.record.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.lims2.server.eln.record.entity.SchemeParameterComponentConfig;
import com.bmos.lims2.server.inspect.scheme.dto.response.ComponentConfigDetailDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: 工序步骤记录项配置 Mapper
 * @Author: yigaohui
 * @Date: 2025/11/10 11:10
 */
@Mapper
public interface SchemeParameterComponentConfigMapper extends BaseMapperX<SchemeParameterComponentConfig> {
    default void deleteByInspectionSchemeParameterConfigId(Long parameterConfigId) {
        LambdaQueryWrapper<SchemeParameterComponentConfig> lambda = new QueryWrapper<SchemeParameterComponentConfig>().lambda();
        lambda.eq(SchemeParameterComponentConfig::getParameterConfigId, parameterConfigId);
        delete(lambda);
    }

    default List<SchemeParameterComponentConfig> selectByParameterConfigId(Long parameterConfigId) {
        return selectList(new QueryWrapper<SchemeParameterComponentConfig>().lambda()
                .eq(SchemeParameterComponentConfig::getParameterConfigId, parameterConfigId));
    }

    List<SchemeParameterComponentConfig> getListByInspectionSchemeParameter(InspectionSchemeParameter inspectionSchemeParameter);

    List<ComponentConfigDetailDTO> selectComponentWithComponentConfig(InspectionSchemeParameter inspectionSchemeParameter);

    List<ComponentConfigDetailDTO> selectStabilityComponentWithComponentConfig(InspectionSchemeParameter inspectionSchemeParameter);
}


