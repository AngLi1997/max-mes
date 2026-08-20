package com.bmos.mes.service.active.controller;

import com.bmos.adaptor.active.RsaVO;
import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.active.service.ActiveService;
import com.bmos.web.interceptor.Activate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.net.SocketException;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
@Validated
public class ActiveController {
    @Autowired
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
