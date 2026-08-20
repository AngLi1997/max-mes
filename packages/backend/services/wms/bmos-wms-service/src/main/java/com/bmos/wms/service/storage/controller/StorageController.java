package com.bmos.wms.service.storage.controller;

import com.bmos.common.response.ResponseInfo;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.wms.service.storage.dto.StorageCreateDTO;
import com.bmos.wms.service.storage.dto.StorageEditDTO;
import com.bmos.wms.service.storage.service.IStorageService;
import com.bmos.wms.service.storage.vo.StorageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 存储区域配置
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 10:47
 */
@RestController
@RequestMapping("/storage/config")
@Validated
@Api(tags = "存储区域配置")
public class StorageController {

    @Resource
    private IStorageService storageConfigService;

    @PostMapping("/create")
    @ApiOperation("新增存储区域")
    @OperationLog
    public ResponseInfo<Void> createStorage(@Validated @RequestBody StorageCreateDTO dto) {
        storageConfigService.createStorage(dto);
        return ResponseInfo.success();
    }

    @PutMapping("/edit")
    @ApiOperation("编辑存储区域")
    @OperationLog
    public ResponseInfo<Void> editStorage(@Validated @RequestBody StorageEditDTO dto) {
        storageConfigService.editStorage(dto);
        return ResponseInfo.success();
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除存储区域")
    @OperationLog
    @ApiImplicitParam(name = "id", value = "id", required = true, example = "1")
    public ResponseInfo<Void> deleteStorage(@RequestParam Long id) {
        storageConfigService.deleteStorage(id);
        return ResponseInfo.success();
    }

    @GetMapping("/queryTree")
    @ApiOperation(value = "根据父级id查询存储区域数据树", notes = "父节点为空则查询根节点")
    @ApiImplicitParam(value = "父级id", name = "parentId", example = "1")
    public ResponseInfo<List<StorageVO>> queryTree(@RequestParam(required = false) Long parentId) {
        return ResponseInfo.success(storageConfigService.queryTree(parentId));
    }

    @GetMapping("/queryTreeWithCargoPosition")
    @ApiOperation(value = "根据父级id查询存储区域数据树(带有货位列表)", notes = "父节点为空则查询根节点")
    @ApiImplicitParam(value = "父级id", name = "parentId", example = "1")
    public ResponseInfo<List<StorageVO>> queryTreeWithCargoPosition(@RequestParam(required = false) Long parentId) {
        return ResponseInfo.success(storageConfigService.queryTreeWithCargoPosition(parentId));
    }

    @GetMapping("/previewTag")
    @ApiOperation(value = "获取预览标签数据")
    public ResponseInfo<Void> getPreviewTag() {
        // todo liang 查询标签预览数据
        return ResponseInfo.success();
    }
}
