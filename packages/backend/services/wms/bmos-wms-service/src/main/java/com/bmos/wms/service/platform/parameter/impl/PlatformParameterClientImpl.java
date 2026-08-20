package com.bmos.wms.service.platform.parameter.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.wms.service.platform.parameter.PlatformParameterClient;
import com.bmos.wms.service.platform.parameter.vo.BusinessParameterDetailVO;
import com.bmos.wms.service.platform.user.FeignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 平台参数读取（与 mes 同款）。
 */
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
            throw new BmosException(com.bmos.common.exception.BaseResponseCode.SERVER_EXCEPTION);
        }
        return vo.getValue();
    }
}
