package com.bmos.mes.service.storage.manage.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.storage.manage.service.MaterialBatchFieldService;
import com.bmos.mes.service.storage.manage.vo.MaterialBatchFieldVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 物料自定义字段controller
 */
@RestController
@RequestMapping("/storage/material/batch/field")
@Validated
@Api(tags = "物料批次自定义字段")
@Slf4j
public class StorageMaterialBatchFieldController {

    @Autowired
    MaterialBatchFieldService materialBatchFieldService;

    @GetMapping("/list/{materialBatchId}")
    @ApiOperation("【物料管理】根据物料批次id查询当前批次的自定义字段信息")
    public ResponseInfo<List<MaterialBatchFieldVO>> queryMaterialField(@Validated  @NotNull @PathVariable("materialBatchId") Long materialBatchId){
        return ResponseInfo.success(materialBatchFieldService.queryMaterialField(materialBatchId));
    }

}
