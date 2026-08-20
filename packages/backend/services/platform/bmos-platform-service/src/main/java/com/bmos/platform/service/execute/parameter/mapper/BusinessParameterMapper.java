package com.bmos.platform.service.execute.parameter.mapper;

import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.platform.service.execute.parameter.dto.BusinessParameterPageDTO;
import com.bmos.platform.service.execute.parameter.model.BusinessParameter;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BusinessParameterMapper extends BaseMapperX<BusinessParameter> {
    /**
     * 分页查询
     * @param dto dto
     * @return List<BusinessParameterPageVO>
     */
    List<BusinessParameterPageVO> page(BusinessParameterPageDTO dto);

    BusinessParameterDetailVO detail(@Param("id") Long id);

    BusinessParameterDetailVO detailByCode(@Param("code")String code);
}
