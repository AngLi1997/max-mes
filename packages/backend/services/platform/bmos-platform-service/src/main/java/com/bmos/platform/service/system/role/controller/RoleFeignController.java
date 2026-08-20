package com.bmos.platform.service.system.role.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.role.feign.RoleFeign;
import com.bmos.platform.facade.system.role.vo.FeignRoleVO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import com.bmos.platform.service.system.role.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feign/role")
@Validated
public class RoleFeignController implements RoleFeign {

    @Autowired
    RoleService roleService;

    @Override
    @GetMapping("/authUserList")
    public ResponseInfo<List<FeignUserVO>> authUserList(String authCode) {
        return ResponseInfo.success(roleService.authUserList(authCode));
    }

    @Override
    @GetMapping("/get/list/ids")
    public ResponseInfo<List<FeignRoleVO>> getListByIds(@RequestParam("ids") List<Long> ids){
        return ResponseInfo.success(roleService.getListByIds(ids));
    }
}
