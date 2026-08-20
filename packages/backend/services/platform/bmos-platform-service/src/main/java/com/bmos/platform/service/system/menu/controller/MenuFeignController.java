package com.bmos.platform.service.system.menu.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.menu.feign.MenuFeign;
import com.bmos.platform.facade.system.menu.vo.MenuVO;
import com.bmos.platform.service.system.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feign/menu")
@Validated
public class MenuFeignController implements MenuFeign {

    @Autowired
    MenuService menuService;

    @Override
    @GetMapping("/getAllChildMenuIdList")
    public ResponseInfo<List<MenuVO>> getAllChildMenuIdList(Long menuId) {
        return ResponseInfo.success(menuService.getAllChildMenuIdList(menuId));
    }
}
