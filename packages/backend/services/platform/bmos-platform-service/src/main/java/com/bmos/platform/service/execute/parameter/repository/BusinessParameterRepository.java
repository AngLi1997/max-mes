package com.bmos.platform.service.execute.parameter.repository;

import com.bmos.platform.service.execute.parameter.vo.BusinessParameterDetailVO;

public interface BusinessParameterRepository {

    /**
     * 详情查询
     * @param code code
     * @return BusinessParameterDetailVO
     */
    BusinessParameterDetailVO detailByCode(String code);

}
