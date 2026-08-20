package com.bmos.platform.service.material.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.platform.facade.material.dto.MaterialTreeNodeVO;
import com.bmos.platform.facade.material.feign.PlatformMaterialFeign;
import com.bmos.platform.service.material.service.MaterialService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/feign/material")
public class PlatformMaterialFeignController implements PlatformMaterialFeign {

    @Resource
    private MaterialService materialService;

    @GetMapping("/tree")
    @ApiOperation("全量物料树(启用状态)")
    public ResponseInfo<List<MaterialTreeNodeVO>> getMaterialTree() {
        return ResponseInfo.success(materialService.getMaterialTree());
    }

}
