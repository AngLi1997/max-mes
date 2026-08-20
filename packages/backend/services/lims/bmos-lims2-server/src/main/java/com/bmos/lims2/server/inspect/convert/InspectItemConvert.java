package com.bmos.lims2.server.inspect.convert;

import com.bmos.lims2.server.inspect.item.dto.InspectItemPageReqDTO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemParamDTO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemParameterDTO;
import com.bmos.lims2.server.inspect.item.dto.InspectItemWithParameterDTO;
import com.bmos.lims2.server.inspect.item.entity.InspectItem;
import com.bmos.lims2.server.inspect.item.entity.InspectItemParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InspectItemConvert {

    InspectItemConvert INSTANCE = Mappers.getMapper(InspectItemConvert.class);

    InspectItem convert2DO(InspectItemWithParameterDTO reqVO);

    InspectItemParameter convert2InspectAnalyzeDO(InspectItemParameterDTO inspectAnalyzeVO);

    InspectItemParamDTO convert2Param(InspectItemPageReqDTO reqVO);


    InspectItemWithParameterDTO convert2InfoRespVO(InspectItem inspectDO);

    InspectItemParameterDTO convert2InspectAnalyzeRespVO(InspectItemParameter analyzeDO);
}
