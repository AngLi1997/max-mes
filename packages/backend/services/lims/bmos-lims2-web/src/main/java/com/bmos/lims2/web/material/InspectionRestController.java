package com.bmos.lims2.web.material;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.feign.material.IssueMaterialFeign;
import com.bmos.lims2.feign.material.dto.RemoteIssueFeignDTO;
import com.bmos.lims2.server.material.service.MaterialService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material")
@Api(tags = "检品feign接口")
@Validated
public class InspectionRestController implements IssueMaterialFeign {

    @Autowired
    MaterialService materialService;

    @PostMapping("/issueMaterialAndCategory")
    public ResponseInfo<Void> issueMaterialAndCategory(@RequestBody RemoteIssueFeignDTO dto) {
        materialService.issueMaterialAndCategory(dto);
        return ResponseInfo.success();
    }

}
