package com.bmos.lims2.server.platform.expression.user.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.platform.expression.user.vo.PlatformUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-platform-user")
public interface PlatformUserOpenFeign {

    @GetMapping("/api/app/platform/user/listByRole")
    ResponseInfo<List<PlatformUserVO>> getListByRole(@RequestParam("roleId") Long roleId);

    @GetMapping("/api/app/platform/user/listByDeptIdList")
    ResponseInfo<List<PlatformUserVO>> listByDeptList(@RequestParam("deptIds") List<Long> deptIds);

    @GetMapping("/api/app/platform/user/listByMenuId")
    ResponseInfo<List<PlatformUserVO>> listByMenuId(@RequestParam("menuId") Long menuId);
}
