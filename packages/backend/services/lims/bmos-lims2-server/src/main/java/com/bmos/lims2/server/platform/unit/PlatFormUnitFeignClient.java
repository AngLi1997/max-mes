package com.bmos.lims2.server.platform.unit;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.platform.unit.dto.CommonUnitDTO;
import com.bmos.lims2.server.platform.unit.dto.UnitQueryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 平台单位feign
 */
@FeignClient(name = "bmos-platform-service", contextId = "bmos-adaptor-platform-unit")
public interface PlatFormUnitFeignClient {
    /**
     * 根据单位id查询单位信息
     * @param userQueryDTO
     * @return
     */
    @PostMapping("/api/app/platform/unit/getUnitById")
    ResponseInfo<CommonUnitDTO> getUnitById(@RequestBody UnitQueryDTO userQueryDTO);
}
