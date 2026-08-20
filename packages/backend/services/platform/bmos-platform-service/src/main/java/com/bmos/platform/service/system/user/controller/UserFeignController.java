package com.bmos.platform.service.system.user.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.user.dto.UserQueryDTO;
import com.bmos.platform.facade.system.user.dto.UserResourceQueryDTO;
import com.bmos.platform.facade.system.user.feign.UserFeign;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.system.user.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequestMapping("/feign/user")
@RestController
@Validated
public class UserFeignController implements UserFeign {

    @Resource
    private UserService userService;

    @Override
    @PostMapping("/listByMenuIdAndDeptIds")
    public ResponseInfo<List<FeignUserVO>> listByMenuIdAndDeptIds(@Validated @RequestBody UserQueryDTO dto) {
        return ResponseInfo.success(userService.listByMenuIdAndDeptIds(dto));
    }

    @Override
    @PostMapping("/listByMenuIdAndResourceId")
    public ResponseInfo<List<FeignUserVO>> listByMenuIdAndResourceId(@Validated @RequestBody UserResourceQueryDTO dto) {
        return ResponseInfo.success(userService.listByMenuIdAndResourceId(dto));
    }

    @Override
    @PostMapping("/listByUserIds")
    public ResponseInfo<Map<String, FeignUserVO>> getByUserIds(@RequestBody Collection<String> userIds) {
        return ResponseInfo.success(userService.getFeignUserByUserIds(userIds));
    }

    @GetMapping("/listUserListByDeptIds")
    @Override
    public ResponseInfo<List<FeignUserVO>> listUserListByDeptIds(List<Long> deptIds) {
        return ResponseInfo.success(userService.listUserListByDeptIds(deptIds));
    }

    @GetMapping("/listUserListByRoleIds")
    @Override
    public ResponseInfo<List<FeignUserVO>> listUserListByRoleIds(List<Long> roles) {
        return ResponseInfo.success(userService.listUserListByRoleIds(roles));
    }

    @Override
    @GetMapping("/getUserByName")
    public ResponseInfo<List<FeignUserVO>> getUserByName(String userName) {
        return ResponseInfo.success(userService.listByName(userName));
    }
}
