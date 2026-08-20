package com.bmos.mes.service.platform.role.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.platform.role.dto.PlatformRoleListQueryDTO;
import com.bmos.mes.service.platform.role.role.PlatformRoleVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-platform-role")
public interface PlatformRoleOpenFeign {

    @GetMapping("/api/app/platform/role/list")
    ResponseInfo<List<PlatformRoleVO>> getRoles(@SpringQueryMap PlatformRoleListQueryDTO dto);

    @GetMapping("/api/app/platform/role/detail/{id}")
    ResponseInfo<PlatformRoleVO> getRole(@PathVariable("id") Long id);
}
