package com.bmos.platform.facade.auth.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.active.dto.LicenseActiveDTO;
import com.bmos.platform.facade.active.dto.LicenseParamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "bmos-platform-service", contextId = "platform-active")
public interface ActiveValidFeign {

    @PostMapping("/api/app/platform/system/active/valid")
    ResponseInfo<LicenseActiveDTO> activeValid(@RequestBody LicenseParamDTO paramDTO);

}
