package com.bmos.wms.service.platform.material.feign;

import com.bmos.common.response.ResponseInfo;
import com.bmos.wms.service.platform.material.dto.*;
import com.bmos.wms.service.platform.material.vo.SyncTreeNodeVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/26 10:16
 */
@FeignClient(name = "bmos-platform-service", contextId = "bmos-adaptor-platform-material")
public interface PlatformMaterialFeignClient {

    /**
     * 校验平台合并编码是否重复
     *
     * @param code
     * @param platformMaterialId
     * @return
     */
    @GetMapping("/api/app/platform/material/existed")
    ResponseInfo<Boolean> checkMergeCodeExisted(@RequestParam("code") String code, @RequestParam("platformMaterialId") Long platformMaterialId);

    /**
     * 平台保存物料分类
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/material/category/save")
    ResponseInfo<Long> saveMaterialCategory(@RequestBody ProductMaterialCategorySaveDTO dto);

    /**
     * 平台保存物料
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/material/save")
    ResponseInfo<Long> saveMaterial(@RequestBody ProductMaterialSaveDTO dto);

    /**
     * 平台取消注册物料
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/material/unregister")
    ResponseInfo<Void> unregisterMaterial(UnregisterMaterialDTO dto);

    /**
     * 平台取消注册物料分类
     *
     * @param dto
     * @return
     */
    @PostMapping("/api/app/platform/material/unregisterCategory")
    ResponseInfo<Void> unregisterCategory(UnregisterMaterialCategoryDTO dto);

    /**
     * 获取同步物料分类树
     *
     * @param parentId
     * @param keyword
     * @return
     */
    @GetMapping("/api/app/platform/material/category/issueTree")
    ResponseInfo<List<SyncTreeNodeVO>> getSyncTree(@NotNull @RequestParam("parentId") Long parentId, @RequestParam("keyword") String keyword);

    /**
     * 获取同步物料分类全量树
     *
     * @return
     */
    @GetMapping("/api/app/platform/material/category/tree")
    ResponseInfo<List<SyncTreeNodeVO>> getSyncTreeAll();

    /**
     * 同步物料
     *
     * @param materialIssueRequestDTO
     * @return
     */
    @PostMapping("/api/app/platform/material/issue")
    ResponseInfo<Void> issueMaterialAndCategory(MaterialIssueRequestDTO materialIssueRequestDTO);
}
