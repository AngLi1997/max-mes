package com.bmos.lims2.web.auth;

import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.active.service.ActiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/active")
@Api(tags = "Lims激活接口")
@Validated
public class ActiveController {

    @Autowired
    ActiveService activeService;

    @PostMapping("/active")
    @ApiOperation("激活")
    public ResponseInfo<String> active(@RequestBody String activeStr) {
        return ResponseInfo.success(activeService.save(activeStr));
    }

    @PostMapping("/actived")
    @ApiOperation("是否激活")
    public ResponseInfo<RsaVO> exists() {
        return ResponseInfo.success(activeService.actived());
    }


}
