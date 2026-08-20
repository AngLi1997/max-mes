package com.bmos.platform.service.system.user.controller;

import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.service.system.user.service.UserSignService;
import com.bmos.platform.service.system.user.service.dto.UserSignSaveDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 签名相关接口
 */
@RestController
@RequestMapping("/user/sign")
@Api(tags = "用户手写签名接口")
@Validated
public class UserSignController {

    @Autowired
    private UserSignService userSignService;

    @GetMapping("/info")
    @ApiOperation("获取当前登录人的签名地址")
    public ResponseInfo<String> getUserSign(){
        return ResponseInfo.success(userSignService.getUserSign(SysUserHolder.getUser().getUserId()));
    }

    @PostMapping("/save")
    @ApiOperation("保存手写签名")
    public ResponseInfo<String> saveUserSign(@RequestBody UserSignSaveDTO dto){
        return ResponseInfo.success(userSignService.saveUserSign(dto.getFileBase64Content(), dto.getSuffix(), SysUserHolder.getUser().getUserId()));
    }

}
