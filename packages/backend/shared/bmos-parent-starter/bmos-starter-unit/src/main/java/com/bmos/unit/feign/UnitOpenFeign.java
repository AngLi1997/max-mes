package com.bmos.unit.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.unit.vo.CommonGlobalUnit;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/26 18:28
 */
@FeignClient(name = "bmos-platform-service", contextId = "unit")
public interface UnitOpenFeign {

    @GetMapping("/api/app/platform/unit/getAllUnit")
    @ApiOperation("查询所有单位列表")
    ResponseInfo<CommonGlobalUnit> getAllUnit();
}
