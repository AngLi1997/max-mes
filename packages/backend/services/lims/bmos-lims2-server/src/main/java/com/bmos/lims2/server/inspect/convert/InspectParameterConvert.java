package com.bmos.lims2.server.inspect.convert;

import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterPageReqDTO;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDTO;
import com.bmos.lims2.server.inspect.parameter.dto.ParameterParamDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectParameter;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InspectParameterConvert {
    InspectParameterConvert INSTANCE = Mappers.getMapper(InspectParameterConvert.class);

    InspectParameter convert2DO(InspectParameterDTO reqVO);

    ParameterParamDTO convert2Param(InspectParameterPageReqDTO reqVO);

    CommonPage<InspectParameterDTO> convert2PageRespVO(CommonPage<InspectParameter> analyzeList);
}
