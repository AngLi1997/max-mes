package com.bmos.platform.service.execute.parameter.repository.impl;

import com.bmos.platform.service.execute.parameter.mapper.BusinessParameterMapper;
import com.bmos.platform.service.execute.parameter.repository.BusinessParameterRepository;
import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessParameterRepositoryImpl implements BusinessParameterRepository {

    @Autowired
    private BusinessParameterMapper businessParameterMapper;

    @Override
    public BusinessParameterDetailVO detailByCode(String code) {
        return businessParameterMapper.detailByCode(code);
    }
}
