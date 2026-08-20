package com.bmos.wms.service.config.active.controller;

import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.web.interceptor.Activate;
import com.bmos.wms.service.config.active.service.ActiveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.net.SocketException;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Api(tags = "激活接口")
@Validated
public class ActiveController {

    @Resource
    private ActiveService activeService;

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

    @GetMapping("/mac")
    @ApiOperation("电脑mac地址")
    public ResponseInfo<Set<String>> mac() throws SocketException {
        return ResponseInfo.success(Activate.getAllMACAddress());
    }
}
