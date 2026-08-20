package com.bmos.platform.service.feign.system.user;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.active.dto.LicenseActiveDTO;
import com.bmos.platform.facade.active.dto.LicenseParamDTO;
import com.bmos.platform.facade.auth.feign.ActiveValidFeign;
import com.bmos.platform.service.system.user.service.ActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/active")
@Validated
public class ActiveValidFeignController implements ActiveValidFeign{

    @Autowired
    ActiveService activeService;

    @PostMapping("/valid")
    public ResponseInfo<LicenseActiveDTO> activeValid(@RequestBody LicenseParamDTO paramDTO){
        return ResponseInfo.success(activeService.valid(paramDTO));
    }

}
