package com.bmos.platform.service.system.user.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.platform.service.system.user.dto.*;
import com.bmos.platform.service.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mobile/user")
@Api(tags = "移动端用户相关接口")
@Validated
public class UserMobileController {

    @Autowired
    private UserService userService;

    @PutMapping("/changePwd")
    @ApiOperation("移动端修改密码功能")
    @OperationLog
    public ResponseInfo<Void> changeMobilePwd(@RequestBody @Validated MobileChangePwdDTO dto) {
        userService.changeMobilePwd(dto);
        return ResponseInfo.success();
    }
}
