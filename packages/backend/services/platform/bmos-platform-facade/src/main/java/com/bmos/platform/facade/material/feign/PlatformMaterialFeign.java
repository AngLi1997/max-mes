package com.bmos.platform.facade.material.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 物料相关feign接口
 */
@FeignClient(name = "bmos-platform-service", contextId = "platform-material")
public interface PlatformMaterialFeign {

    @GetMapping("/api/app/platform/feign/material/tree")
    @ApiOperation("获取启用物料树")
    ResponseInfo<List<MaterialTreeNodeVO>> getMaterialTree();

}
