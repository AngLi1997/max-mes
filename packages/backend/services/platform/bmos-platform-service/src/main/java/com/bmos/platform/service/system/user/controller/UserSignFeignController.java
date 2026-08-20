package com.bmos.platform.service.system.user.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.user.dto.UserSignSaveFeignDTO;
import com.bmos.platform.facade.system.user.feign.UserSignFeign;
import com.bmos.platform.service.system.user.service.UserSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feign/user/sign")
@Validated
public class UserSignFeignController implements UserSignFeign {

    @Autowired
    UserSignService userSignService;

    @Override
    @PostMapping("/save")
    public ResponseInfo<String> saveUserSign(@RequestBody UserSignSaveFeignDTO dto) {
        return ResponseInfo.success(userSignService.saveUserSign(dto.getFileBase64Content(), dto.getSuffix(), dto.getUserId()));
    }

    @Override
    @GetMapping("/info")
    public ResponseInfo<String> getUserSign(String userId) {
        return ResponseInfo.success(userSignService.getUserSign(userId));
    }
}
