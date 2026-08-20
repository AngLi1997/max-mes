package com.bmos.mes.service.tag.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.tag.dto.CargoPositionTagQuery;
import com.bmos.mes.service.tag.dto.ScanTareWeighDTO;
import com.bmos.mes.service.tag.dto.StorageMaterialTagQuery;
import com.bmos.mes.service.tag.service.ITagService;
import com.bmos.mes.service.tag.vo.CargoPositionTag;
import com.bmos.mes.service.tag.vo.PreparationProduceStorageMaterialTag;
import com.bmos.mes.service.tag.vo.StorageMaterialTag;
import com.bmos.mes.service.tag.vo.TareWeighTag;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 标签打印接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/15 10:37
 */
@RestController
@RequestMapping("/tag/print")
@Validated
@Api(tags = "标签打印接口")
public class TagController {

    @Resource
    private ITagService tagService;

    @PostMapping("/STORAGE_MATERIAL")
    @ApiOperation("打印物料件标签")
    public ResponseInfo<StorageMaterialTag> queryStorageMaterialByStorageMaterialNo(@Validated @RequestBody StorageMaterialTagQuery query) {
        return ResponseInfo.success(tagService.queryStorageMaterialByStorageMaterialNo(query));
    }

    @PostMapping("/CARGO_POSITION")
    @ApiOperation("打印暂存货位标签")
    public ResponseInfo<CargoPositionTag> queryCargoPositionByPositionNo(@Validated @RequestBody CargoPositionTagQuery query) {
        return ResponseInfo.success(tagService.queryCargoPositionByPositionNo(query));
    }

    @PostMapping("/PREPARATION_PRODUCE_STORAGE_MATERIAL")
    @ApiOperation("打印配液产出物料件标签")
    public ResponseInfo<PreparationProduceStorageMaterialTag> queryPreparationProduceStorageMaterial(@Validated @RequestBody StorageMaterialTagQuery query) {
        return ResponseInfo.success(tagService.queryPreparationProduceStorageMaterial(query));
    }

    @PostMapping("/TARE_WEIGH")
    @ApiOperation("打印皮重标签")
    public ResponseInfo<TareWeighTag> queryTareWeighByTareWeighId(@Validated @RequestBody ScanTareWeighDTO query) {
        return ResponseInfo.success(tagService.queryTareWeighByTareWeighId(query));
    }
}
