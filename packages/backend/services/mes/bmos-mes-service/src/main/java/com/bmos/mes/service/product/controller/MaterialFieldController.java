package com.bmos.mes.service.product.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.product.dto.MaterialFieldQueryDTO;
import com.bmos.mes.service.product.service.MaterialFieldService;
import com.bmos.mes.service.product.vo.MaterialFieldInfoVO;
import com.bmos.mes.service.product.vo.MaterialFieldTypeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物料自定义字段
 */
@RestController
@RequestMapping("/material/field")
@Api(tags = "【生产物料】物料自定义字段")
public class MaterialFieldController {

    @Autowired
    private MaterialFieldService materialFieldService;


    @GetMapping("/list")
    @ApiOperation("【生产物料】获取生产物料的能够配置的自定义字段")
    public ResponseInfo<List<MaterialFieldTypeVO>> getMaterialFieldList(){
        return ResponseInfo.success(materialFieldService.getMaterialFieldList());
    }

    @GetMapping("/info/{materialId}")
    @ApiOperation("【生产物料】根据生产物料的自定义字段信息")
    public ResponseInfo<List<MaterialFieldInfoVO>> getMaterialFieldInfo(@PathVariable("materialId") Long materialId){
        return ResponseInfo.success(materialFieldService.getMaterialFieldInfo(materialId));
    }

    @GetMapping("/info/list")
    @ApiOperation("【生产物料】获取生产物料的自定义字段信息")
    public ResponseInfo<List<MaterialFieldInfoVO>> getMaterialFieldInfo(@Validated MaterialFieldQueryDTO dto) {
        return ResponseInfo.success(materialFieldService.getMaterialFieldInfo(dto));
    }
}
