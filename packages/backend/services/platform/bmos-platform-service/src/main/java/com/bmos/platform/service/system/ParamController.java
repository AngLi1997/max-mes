package com.bmos.platform.service.system;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.service.config.web.VersionConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/param")
@Api(tags = "参数获取")
public class ParamController {

    @Autowired
    private VersionConfig versionConfig;


    @GetMapping("/app/version")
    @ApiOperation("App版本获取")
    public ResponseInfo<String> getAppVersion(){
        return ResponseInfo.success(Optional.ofNullable(versionConfig).orElse(new VersionConfig()).getSystemVersion());
    }

}
