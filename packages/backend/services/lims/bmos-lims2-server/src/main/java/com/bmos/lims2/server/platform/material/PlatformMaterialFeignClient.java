package com.bmos.lims2.server.platform.material;

import com.bmos.common.response.ResponseInfo;

import com.bmos.lims2.server.material.dto.MaterialIssueRequestDTO;
import com.bmos.lims2.server.platform.material.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 平台物料的feign接口
 */
@FeignClient(name = "bmos-platform-service", contextId = "bmos-adaptor-platform-material")
public interface PlatformMaterialFeignClient {

    /**
     * 新增物料分类
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/material/category/save")
    ResponseInfo<Long> saveMaterialCategory(@RequestBody ProductMaterialCategorySaveDTO dto);

    /**
     * 更新物料分类
     * @param dto
     * @return
     */
    @PutMapping("/api/app/platform/material/category/update")
    ResponseInfo<Void> updateMaterialCategory(@RequestBody ProductMaterialCategoryUpdateDTO dto);

    /**
     * 保存物料信息
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/material/save")
    ResponseInfo<Long> saveMaterial(@RequestBody ProductMaterialSaveDTO dto);

    /**
     *  检测物料是否存在
     * @param code: 物料编码
     * @param platformMaterialId：物料id
     * @return
     */
    @GetMapping("/api/app/platform/material/existed")
    ResponseInfo<Boolean> checkMaterialExisted(@RequestParam("code") String code, @RequestParam("platformMaterialId") Long platformMaterialId);

    /**
     * 检测物料分类是否存在
     * @param code：物料分类code
     * @param id：物料分类id
     * @return
     */
    @GetMapping("/api/app/platform/material/category/existed")
    ResponseInfo<Boolean> checkCategoryExisted(@RequestParam(value = "code") String code, @RequestParam(value = "id") Long id);

    /**
     * 更新物料信息
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/material/update")
    ResponseInfo<Void> updateMaterial(@RequestBody ProductMaterialUpdateDTO dto);

    /**
     * 获取同步分类物料树
     * @param parentId
     * @param keyword
     * @return
     */
    @GetMapping("/api/app/platform/material/category/issueTree")
    ResponseInfo<List<SyncTreeNodeDTO>> getSyncTree(@NotNull @RequestParam("parentId") Long parentId, @RequestParam("keyword") String keyword);

    /**
     * 获取同步物料信息
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/material/syncList")
    ResponseInfo<RemoteSyncDTO> getSyncMaterialInfo(@RequestBody SyncMaterialInfoDTO dto);

    /**
     * 获取全量物料信息分类树
     * @return
     */
    @GetMapping("/api/app/platform/material/category/tree")
    ResponseInfo<List<SyncTreeNodeDTO>> getSyncTreeAll();

    @PostMapping("/api/app/platform/material/unregister")
    ResponseInfo<Void> unregisterMaterial(UnregisterMaterialDTO dto);

    @PostMapping("/api/app/platform/material/unregisterCategory")
    ResponseInfo<Void> unregisterCategory(UnregisterMaterialCategoryDTO dto);

    @GetMapping("/api/app/platform/material/tree")
    ResponseInfo<List<SyncTreeNodeDTO>> getMaterialTree();

    @GetMapping("/api/app/platform/material/existed")
    ResponseInfo<Boolean> checkMergeCodeExisted(@RequestParam("code") String code, @RequestParam("platformMaterialId") Long platformMaterialId);


    @PostMapping("/api/app/platform/material/issue")
    ResponseInfo<Void> issueMaterialAndCategory(MaterialIssueRequestDTO materialIssueRequestDTO);
}
