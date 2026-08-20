package com.bmos.platform.facade.system.menu.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.system.menu.vo.MenuVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "bmos-platform-service", contextId = "platform-system-menu")
public interface MenuFeign {

    @GetMapping("/api/app/platform/feign/menu/getAllChildMenuIdList")
    ResponseInfo<List<MenuVO>> getAllChildMenuIdList(@RequestParam("menuId") Long menuId);

}
