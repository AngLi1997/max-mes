package com.bmos.mes.service.platform.product;

import com.bmos.common.response.ResponseInfo;
import com.bmos.mes.service.product.dto.*;
import com.bmos.mes.service.product.vo.SyncTreeNodeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "bmos-platform-service", contextId = "bmos-adaptor-platform-material")
public interface PlatformMaterialFeignClient {

    @GetMapping("/api/app/platform/")
    ResponseInfo<List<Long>> getRolesIds();

    @GetMapping("/api/app/platform/")
    ResponseInfo<List<Long>> getDeptIds();

    @PostMapping("/api/app/platform/material/category/save")
    ResponseInfo<Long> saveMaterialCategory(@RequestBody ProductMaterialCategorySaveDTO dto);

    @PutMapping("/api/app/platform/material/category/update")
    ResponseInfo<Void> updateMaterialCategory(@RequestBody ProductMaterialCategoryUpdateDTO dto);

    @PostMapping("/api/app/platform/material/save")
    ResponseInfo<Long> saveMaterial(@RequestBody ProductMaterialSaveDTO dto);

    @GetMapping("/api/app/platform/material/existed")
    ResponseInfo<Boolean> checkMergeCodeExisted(@RequestParam("code") String code, @RequestParam("platformMaterialId") Long platformMaterialId);

    @PostMapping("/api/app/platform/material/update")
    ResponseInfo<Void> updateMaterial(@RequestBody ProductMaterialUpdateDTO dto);

    @GetMapping("/api/app/platform/material/category/issueTree")
    ResponseInfo<List<SyncTreeNodeVO>> getSyncTree(@NotNull @RequestParam("parentId") Long parentId,@RequestParam("keyword") String keyword);

    @PostMapping("/api/app/platform/material/syncList")
    ResponseInfo<RemoteSyncDTO> getSyncMaterialInfo(@RequestBody SyncMaterialInfoDTO dto);

    @GetMapping("/api/app/platform/material/category/tree")
    ResponseInfo<List<SyncTreeNodeVO>> getSyncTreeAll();

    @PostMapping("/api/app/platform/material/unregister")
    ResponseInfo<Void> unregisterMaterial(UnregisterMaterialDTO dto);

    @PostMapping("/api/app/platform/material/unregisterCategory")
    ResponseInfo<Void> unregisterCategory(UnregisterMaterialCategoryDTO dto);

    @PostMapping("/api/app/platform/material/issue")
    ResponseInfo<Void> issueMaterialAndCategory(MaterialIssueRequestDTO materialIssueRequestDTO);
}
