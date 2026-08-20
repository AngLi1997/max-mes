package com.bmos.wms.service.platform.user.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.wms.service.platform.user.vo.PlatformUserVO;
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

}
