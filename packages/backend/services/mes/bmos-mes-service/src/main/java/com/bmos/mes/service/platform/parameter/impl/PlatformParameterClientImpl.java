package com.bmos.mes.service.platform.parameter.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.platform.parameter.PlatformParameterClient;
import com.bmos.mes.service.platform.parameter.vo.BusinessParameterDetailVO;
import com.bmos.mes.service.platform.plan.vo.NextCodeVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;

@Service
@Slf4j
public class PlatformParameterClientImpl {
    @Autowired
    private PlatformParameterClient platformParameterClient;

    public String getValueByCode(String code) {
        ResponseInfo<BusinessParameterDetailVO> responseInfo = FeignUtils
            .handleRequest(data -> platformParameterClient.detail(data), code);
        BusinessParameterDetailVO vo = responseInfo.getData();
        if (Objects.isNull(vo)) {
            throw new BmosException(MesResponseCode.PARAMETER_QUERY_NOT_EXISTS);
        }
        return vo.getValue();
    }
}
