package com.bmos.mes.service.storage.config.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.storage.config.dto.StorageCreateDTO;
import com.bmos.mes.service.storage.config.dto.StorageEditDTO;
import com.bmos.mes.service.storage.config.service.IStorageConfigService;
import com.bmos.mes.service.storage.config.vo.StorageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 暂存间配置
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:47
 */
@RestController
@RequestMapping("/storage/config")
@Validated
@Api(tags = "暂存间配置")
public class StorageConfigController {

    @Resource
    private IStorageConfigService storageConfigService;

    @PostMapping("/create")
    @ApiOperation("新增暂存间")
    @OperationLog
    public ResponseInfo<Void> createStorage(@Validated @RequestBody StorageCreateDTO dto) {
        storageConfigService.createStorage(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/edit")
    @ApiOperation("编辑暂存间")
    @OperationLog
    public ResponseInfo<Void> editStorage(@Validated @RequestBody StorageEditDTO dto) {
        storageConfigService.editStorage(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除暂存间")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "id", required = true, example = "1")
    public ResponseInfo<Void> deleteStorage(@RequestParam Long id) {
        storageConfigService.deleteStorage(id);
        return ResponseInfo.success();
    }

    @GetMapping("/queryList")
    @ApiOperation(value = "根据父级节点查询暂存间列表", notes = "父节点为空则查询根节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父级id", example = "1"),
            @ApiImplicitParam(name = "keyword", value = "查询关键词", example = "培养室暂存间")
    })
    public ResponseInfo<List<StorageVO>> queryList(@RequestParam(required = false) Long parentId,
                                                   @RequestParam(required = false) String keyword) {
        return ResponseInfo.success(storageConfigService.queryList(parentId, keyword));
    }

    @GetMapping("/queryTree")
    @ApiOperation(value = "根据父级id查询暂存间数据树", notes = "父节点为空则查询根节点")
    @ApiImplicitParam(value = "父级id", name = "parentId", example = "1")
    public ResponseInfo<List<StorageVO>> queryTree(@RequestParam(required = false) Long parentId) {
        return ResponseInfo.success(storageConfigService.queryTree(parentId));
    }

    @GetMapping("/queryTreeWithCargoPosition")
    @ApiOperation(value = "根据父级id查询暂存间数据树(带有货位列表)", notes = "父节点为空则查询根节点")
    @ApiImplicitParam(value = "父级id", name = "parentId", example = "1")
    public ResponseInfo<List<StorageVO>> queryTreeWithCargoPosition(@RequestParam(required = false) Long parentId) {
        return ResponseInfo.success(storageConfigService.queryTreeWithCargoPosition(parentId));
    }
}
