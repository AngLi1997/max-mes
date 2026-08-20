package com.bmos.platform.service.execute.parameter.convert;

import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.bmos.platform.service.execute.parameter.dto.UpdateBusinessParameterDTO;
import com.bmos.platform.service.execute.parameter.model.BusinessParameter;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BusinessParameterConverter {
    BusinessParameterConverter INSTANCE = Mappers.getMapper(BusinessParameterConverter.class);
    BusinessParameter convertDO(UpdateBusinessParameterDTO dto);

    BusinessParameterDetailFeignVO convertFeignVO(BusinessParameterDetailVO businessParameterDetailVO);

    List<BusinessParameterVO> convertVO(List<BusinessParameter> businessParameters);
}
