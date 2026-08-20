package com.bmos.lims2.feign.material;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.feign.material.dto.RemoteIssueFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @className: IssueMaterialFeign
 * @author: yigaohui
 * @date: 2025/7/14 18:04
 * @Version: 1.0
 * @description:
 */
@FeignClient(name = "bmos-lims2-service", contextId = "bmos-lims2-material")
public interface IssueMaterialFeign {
    @PostMapping("/material/issueMaterialAndCategory")
    ResponseInfo<Void> issueMaterialAndCategory(@RequestBody RemoteIssueFeignDTO dto);
}