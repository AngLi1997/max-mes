package com.bmos.mes.service.product.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.material.feign.MaterialFeign;
import com.bmos.mes.material.vo.MaterialFieldInfoFeignVO;
import com.bmos.mes.service.product.service.MaterialFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物料自定义字段
 */
@RestController
@RequestMapping("/feign/material")
public class MaterialFeignController implements MaterialFeign {

    @Autowired
    private MaterialFieldService materialFieldService;

    @Override
    @GetMapping("/field/info")
    public ResponseInfo<List<MaterialFieldInfoFeignVO>> getMaterialFieldFeignInfo(Long materialId) {
        return ResponseInfo.success(materialFieldService.getMaterialFieldFeignInfo(materialId));
    }
}
