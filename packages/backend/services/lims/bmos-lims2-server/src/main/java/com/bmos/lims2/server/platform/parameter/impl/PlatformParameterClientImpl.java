package com.bmos.lims2.server.platform.parameter.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.platform.parameter.PlatformParameterClient;
import com.bmos.lims2.server.platform.parameter.vo.BusinessParameterDetailVO;
import com.bmos.lims2.server.platform.util.FeignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new BmosException(LimsResponseCode.PARAMETER_QUERY_NOT_EXISTS);
        }
        return vo.getValue();
    }
}
