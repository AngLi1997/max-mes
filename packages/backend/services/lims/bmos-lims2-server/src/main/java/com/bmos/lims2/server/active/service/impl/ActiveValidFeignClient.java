package com.bmos.lims2.server.active.service.impl;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.response.ResponseItem;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.platform.facade.active.dto.LicenseActiveDTO;
import com.bmos.platform.facade.active.dto.LicenseParamDTO;
import com.bmos.platform.facade.auth.feign.ActiveValidFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class ActiveValidFeignClient {

    @Autowired
    ActiveValidFeign activeValidFeign;

    /**
     * license认证校验
     * @param paramDTO
     * @return
     */
    public LicenseActiveDTO activeValid(LicenseParamDTO paramDTO) {
        ResponseInfo<LicenseActiveDTO> responseInfo = null;
        try {
            responseInfo = activeValidFeign.activeValid(paramDTO);
            log.info("调用平台激活码认证校验Feign成功 response={}", JsonUtils.toJsonString(responseInfo));
        } catch (Exception e) {
            log.error("调用平台激活码认证校验Feign失败 paramDTO={}", JsonUtils.toJsonString(paramDTO), e);
            throw new BmosException(LimsResponseCode.PLATFORM_GET_SYNC_ERROR);
        }
        if (Objects.isNull(responseInfo) || responseInfo.isError()) {
            log.error(JsonUtils.toJsonString(responseInfo));
            throw new BmosException(new ResponseItem(responseInfo.getCode(), responseInfo.getMessage(), "调用"));
        }
        return responseInfo.getData();
    }

}
