package com.bmos.platform.service.execute.parameter.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.platform.common.exception.PlatformResponseCode;
import com.bmos.platform.facade.system.execute.parameter.vo.BusinessParameterDetailFeignVO;
import com.bmos.platform.service.execute.parameter.convert.BusinessParameterConverter;
import com.bmos.platform.service.execute.parameter.dto.BusinessParameterPageDTO;
import com.bmos.platform.service.execute.parameter.dto.UpdateBusinessParameterDTO;
import com.bmos.platform.service.execute.parameter.mapper.BusinessParameterMapper;
import com.bmos.platform.service.execute.parameter.model.BusinessParameter;
import com.bmos.platform.service.execute.parameter.service.BusinessParameterService;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterPageVO;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class BusinessParameterServiceImpl implements BusinessParameterService {

    @Autowired
    private BusinessParameterMapper businessParameterMapper;

    @Override
    public List<BusinessParameterPageVO> page(BusinessParameterPageDTO dto) {
        return businessParameterMapper.page(dto);
    }

    @Override
    public BusinessParameterDetailVO detail(Long id) {
        return businessParameterMapper.detail(id);
    }

    @Override
    public BusinessParameterDetailVO detailByCode(String code) {
        return businessParameterMapper.detailByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UpdateBusinessParameterDTO dto) {
        BusinessParameter businessParameter = businessParameterMapper.selectById(dto.getId());
        if (Objects.isNull(businessParameter)) {
            throw new BmosException(PlatformResponseCode.DETAIL_IS_NULL);
        }
        dto.validType(businessParameter.getValueType());
        businessParameter.setValue(dto.getValue());
        businessParameterMapper.updateById(businessParameter);
    }

    @Override
    public BusinessParameterDetailFeignVO detailFeignByCode(String code) {
        BusinessParameterDetailVO businessParameterDetailVO = this.detailByCode(code);
        return BusinessParameterConverter.INSTANCE.convertFeignVO(businessParameterDetailVO);
    }

    @Override
    public List<BusinessParameterVO> getAllParam() {
        List<BusinessParameter> businessParameters = businessParameterMapper.selectList();
        return BusinessParameterConverter.INSTANCE.convertVO(businessParameters);
    }
}
