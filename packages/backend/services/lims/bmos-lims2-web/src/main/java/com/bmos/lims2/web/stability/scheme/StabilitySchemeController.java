package com.bmos.lims2.web.stability.scheme;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeCopyFromVersionDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeItemSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeDataPointBatchUpdateDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeJudgmentBatchUpdateDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemePlanSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionSaveItemsDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeBasicSaveRespDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeItemDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeParameterDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemePlanDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionDTO;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeItemService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemePlanService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeService;
import com.bmos.lims2.server.stability.scheme.service.StabilitySchemeVersionService;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeBasicSaveReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeCopyFromVersionReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeCompleteSaveReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeDataPointBatchUpdateReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeJudgmentBatchUpdateReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeParameterExecuteMethodUpdateReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeQueryReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemePlanSaveReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeSaveFusionReqVO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeSaveFusionDTO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemePermissionReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeVersionSaveItemsReqVO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemeVersionAuditQueryDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionAuditDTO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeVersionAuditQueryReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeVersionCopyReqVO;
import com.bmos.lims2.web.stability.scheme.vo.request.StabilitySchemeVersionQueryReqVO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeVersionFullConfigDTO;
import com.bmos.lims2.web.stability.scheme.vo.response.*;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 稳定性方案Controller
 *
 * @author makejava
 * @since 2025-03-17 10:00:00
 */
@RestController
@RequestMapping("/stability-scheme")
@Api(tags = "稳定性方案-接口")
@Validated
public class StabilitySchemeController {

    @Autowired
    private StabilitySchemeService stabilitySchemeService;

    @Autowired
    private StabilitySchemeVersionService stabilitySchemeVersionService;

    @Autowired
    private StabilitySchemeItemService stabilitySchemeItemService;

    @Autowired
    private StabilitySchemePlanService stabilitySchemePlanService;

    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

    @GetMapping("/page")
    @ApiOperation("分页查询稳定性方案列表")
    public ResponseInfo<CommonPage<StabilitySchemeListRespVO>> pageStabilityScheme(@Validated StabilitySchemeQueryReqVO reqVO) {
        StabilitySchemeQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilitySchemeQueryDTO.class);
        // 解析物料ID集合：优先 materialId，其次 categoryId（取分类下启用检品）
        java.util.List<Long> materialIds = null;
        if (reqVO.getMaterialId() != null) {
            materialIds = java.util.Collections.singletonList(reqVO.getMaterialId());
        } else if (reqVO.getCategoryId() != null) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials = materialService.getAllByCategoryId(reqVO.getCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                materialIds = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        // 按分类查询但分类下无启用检品，直接返回空分页
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<StabilitySchemeListRespVO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }
        CommonPage<StabilitySchemeDTO> page = stabilitySchemeService.pageStabilityScheme(queryDTO);

        CommonPage<StabilitySchemeListRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), StabilitySchemeListRespVO.class));

        return ResponseInfo.success(respPage);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询稳定性方案")
    public ResponseInfo<StabilitySchemeRespVO> getStabilityScheme(@PathVariable Long id) {
        StabilitySchemeDTO dto = stabilitySchemeService.getStabilityScheme(id);
        StabilitySchemeRespVO vo = BeanUtil.copyProperties(dto, StabilitySchemeRespVO.class);
        if (dto.getMaterialId() != null) {
            StabilitySchemeRespVO.MaterialInfoRespVO material = new StabilitySchemeRespVO.MaterialInfoRespVO();
            material.setMaterialId(dto.getMaterialId());
            material.setMaterialName(dto.getMaterialName());
            material.setMaterialCode(dto.getMaterialCode());
            vo.setMaterial(material);
        }
        return ResponseInfo.success(vo);
    }

    @PostMapping("/save")
    @ApiOperation("保存稳定性方案基础信息（生成方案）")
    public ResponseInfo<StabilitySchemeBasicSaveRespVO> saveStabilityScheme(
            @RequestBody @Validated StabilitySchemeBasicSaveReqVO reqVO) {
        StabilitySchemeSaveDTO saveDTO = BeanUtil.copyProperties(reqVO, StabilitySchemeSaveDTO.class);
        saveDTO.setId(reqVO.getSchemeId());

        if (reqVO.getMaterial() != null) {
            StabilitySchemeSaveDTO.MaterialInfoDTO materialDTO = new StabilitySchemeSaveDTO.MaterialInfoDTO();
            materialDTO.setMaterialId(reqVO.getMaterial().getMaterialId());
            materialDTO.setMaterialName(reqVO.getMaterial().getMaterialName());
            materialDTO.setMaterialCode(reqVO.getMaterial().getMaterialCode());
            saveDTO.setMaterial(materialDTO);
        }
        saveDTO.setDeptIds(reqVO.getDeptIds());

        StabilitySchemeBasicSaveRespDTO respDTO = stabilitySchemeService.saveStabilityScheme(saveDTO);
        return ResponseInfo.success(BeanUtil.copyProperties(respDTO, StabilitySchemeBasicSaveRespVO.class));
    }

    @PostMapping("/save-fusion")
    @ApiOperation("全量暂存稳定性方案（基础信息+分析项+检验计划，不校验必填）")
    public ResponseInfo<StabilitySchemeBasicSaveRespVO> saveStabilitySchemeFusion(
            @RequestBody StabilitySchemeSaveFusionReqVO reqVO) {
        StabilitySchemeSaveFusionDTO fusionDTO = new StabilitySchemeSaveFusionDTO();
        fusionDTO.setBasic(BeanUtil.copyProperties(reqVO.getBasic(), StabilitySchemeSaveDTO.class));
        if (reqVO.getBasic() != null) {
            fusionDTO.getBasic().setId(reqVO.getBasic().getSchemeId());
        }
        if (reqVO.getItemUpdates() != null) {
            fusionDTO.setItemUpdates(reqVO.getItemUpdates().stream().map(itemVO -> {
                StabilitySchemeItemSaveDTO.ItemDTO itemDTO = BeanUtil.copyProperties(itemVO,
                        StabilitySchemeItemSaveDTO.ItemDTO.class, "parameters");
                if (itemVO.getInspectionParameters() != null) {
                    itemDTO.setInspectionParameters(BeanUtil.copyToList(itemVO.getInspectionParameters(),
                            StabilitySchemeItemSaveDTO.ParameterDTO.class));
                }
                return itemDTO;
            }).collect(Collectors.toList()));
        }
        // 稳定性计划（稳定性方案特有，对应检验方案的取样配置）
        if (reqVO.getPlans() != null) {
            StabilitySchemePlanSaveDTO planSaveDTO = new StabilitySchemePlanSaveDTO();
            planSaveDTO.setPlans(reqVO.getPlans().stream().map(planVO -> {
                StabilitySchemePlanSaveDTO.PlanDTO planDTO = BeanUtil.copyProperties(planVO,
                        StabilitySchemePlanSaveDTO.PlanDTO.class, "timepoints");
                if (planVO.getTimepoints() != null) {
                    planDTO.setTimepoints(planVO.getTimepoints().stream().map(tpVO -> {
                        StabilitySchemePlanSaveDTO.TimepointDTO tpDTO = BeanUtil.copyProperties(tpVO,
                                StabilitySchemePlanSaveDTO.TimepointDTO.class, "paramRefs");
                        if (tpVO.getParamRefs() != null) {
                            tpDTO.setParamRefs(BeanUtil.copyToList(tpVO.getParamRefs(),
                                    StabilitySchemePlanSaveDTO.ParamRefDTO.class));
                        }
                        return tpDTO;
                    }).collect(Collectors.toList()));
                }
                return planDTO;
            }).collect(Collectors.toList()));
            fusionDTO.setPlanSave(planSaveDTO);
        }
        return ResponseInfo.success(BeanUtil.copyProperties(
                stabilitySchemeService.saveStabilitySchemeFusion(fusionDTO), StabilitySchemeBasicSaveRespVO.class));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除稳定性方案")
    public ResponseInfo<Void> deleteStabilityScheme(@PathVariable Long id) {
        stabilitySchemeService.deleteStabilityScheme(id);
        return ResponseInfo.success();
    }

    @GetMapping("/list")
    @ApiOperation("查询所有稳定性方案列表")
    public ResponseInfo<List<StabilitySchemeRespVO>> listAll() {
        List<StabilitySchemeDTO> dtoList = stabilitySchemeService.listAll();
        List<StabilitySchemeRespVO> voList = dtoList.stream()
                .map(dto -> {
                    StabilitySchemeRespVO vo = BeanUtil.copyProperties(dto, StabilitySchemeRespVO.class);
                    if (dto.getMaterialId() != null) {
                        StabilitySchemeRespVO.MaterialInfoRespVO material = new StabilitySchemeRespVO.MaterialInfoRespVO();
                        material.setMaterialId(dto.getMaterialId());
                        material.setMaterialName(dto.getMaterialName());
                        material.setMaterialCode(dto.getMaterialCode());
                        vo.setMaterial(material);
                    }
                    return vo;
                })
                .collect(Collectors.toList());
        return ResponseInfo.success(voList);
    }

    @GetMapping("/version/page")
    @ApiOperation("分页查询稳定性方案版本列表")
    public ResponseInfo<CommonPage<StabilitySchemeVersionRespVO>> pageStabilitySchemeVersion(
            @Validated StabilitySchemeVersionQueryReqVO reqVO) {
        StabilitySchemeVersionQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilitySchemeVersionQueryDTO.class);
        CommonPage<StabilitySchemeVersionDTO> page = stabilitySchemeVersionService.pageStabilitySchemeVersion(queryDTO);

        CommonPage<StabilitySchemeVersionRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), StabilitySchemeVersionRespVO.class));

        return ResponseInfo.success(respPage);
    }

    @GetMapping("/version/{id}")
    @ApiOperation("根据ID查询稳定性方案版本")
    public ResponseInfo<StabilitySchemeVersionRespVO> getStabilitySchemeVersion(@PathVariable Long id) {
        StabilitySchemeVersionDTO dto = stabilitySchemeVersionService.getStabilitySchemeVersion(id);
        StabilitySchemeVersionRespVO stabilitySchemeVersionRespVO = BeanUtil.copyProperties(dto, StabilitySchemeVersionRespVO.class);
        StabilitySchemeVersionRespVO.MaterialInfoRespVO material = new StabilitySchemeVersionRespVO.MaterialInfoRespVO();
        material.setMaterialId(dto.getMaterialId());
        material.setMaterialName(dto.getMaterialName());
        material.setMaterialCode(dto.getMaterialCode());
        stabilitySchemeVersionRespVO.setMaterial(material);
        return ResponseInfo.success(stabilitySchemeVersionRespVO);
    }

    @GetMapping("/version/{id}/full-config")
    @ApiOperation("获取稳定性方案版本完整配置（基础信息+检验项目+检验计划，用于编辑和查看明细）")
    public ResponseInfo<StabilitySchemeVersionFullConfigRespVO> getVersionFullConfig(@PathVariable Long id) {
        StabilitySchemeVersionFullConfigDTO dto = stabilitySchemeVersionService.getVersionFullConfig(id);
        StabilitySchemeVersionFullConfigRespVO vo = BeanUtil.copyProperties(dto, StabilitySchemeVersionFullConfigRespVO.class);
        if (dto.getMaterial() != null) {
            StabilitySchemeVersionFullConfigRespVO.MaterialInfoRespVO material = new StabilitySchemeVersionFullConfigRespVO.MaterialInfoRespVO();
            material.setMaterialId(dto.getMaterial().getMaterialId());
            material.setMaterialName(dto.getMaterial().getMaterialName());
            material.setMaterialCode(dto.getMaterial().getMaterialCode());
            vo.setMaterial(material);
        }
        vo.setInspectionItems(toItemRespVOList(dto.getInspectionItems()));
        vo.setPlans(BeanUtil.copyToList(dto.getPlans(), StabilitySchemePlanRespVO.class));
        return ResponseInfo.success(vo);
    }

    @GetMapping("/version/{id}/basic")
    @ApiOperation("获取稳定性方案版本基础信息（版本信息、检品）")
    public ResponseInfo<StabilitySchemeVersionBasicConfigRespVO> getVersionBasicConfig(@PathVariable Long id) {
        StabilitySchemeVersionFullConfigDTO dto = stabilitySchemeVersionService.getVersionBasicConfig(id);
        StabilitySchemeVersionBasicConfigRespVO vo = BeanUtil.copyProperties(dto, StabilitySchemeVersionBasicConfigRespVO.class);
        if (dto.getMaterial() != null) {
            StabilitySchemeVersionBasicConfigRespVO.MaterialInfoRespVO material = new StabilitySchemeVersionBasicConfigRespVO.MaterialInfoRespVO();
            material.setMaterialId(dto.getMaterial().getMaterialId());
            material.setMaterialName(dto.getMaterial().getMaterialName());
            material.setMaterialCode(dto.getMaterial().getMaterialCode());
            vo.setMaterial(material);
        }
        return ResponseInfo.success(vo);
    }

    @GetMapping("/version/list/{schemeId}")
    @ApiOperation("根据方案ID查询版本列表")
    public ResponseInfo<List<StabilitySchemeVersionRespVO>> listVersionsBySchemeId(@PathVariable Long schemeId) {
        List<StabilitySchemeVersionDTO> dtoList = stabilitySchemeVersionService.listBySchemeId(schemeId);
        List<StabilitySchemeVersionRespVO> voList = dtoList.stream()
                .map(dto -> BeanUtil.copyProperties(dto, StabilitySchemeVersionRespVO.class))
                .collect(Collectors.toList());
        return ResponseInfo.success(voList);
    }

    @GetMapping("/version/active/list")
    @ApiOperation("根据物料ID查询对应稳定性方案的生效版本列表（含方案基本信息）")
    public ResponseInfo<List<StabilitySchemeVersionRespVO>> listActiveVersionsByMaterialId(
            @RequestParam Long materialId) {
        List<StabilitySchemeVersionDTO> dtoList = stabilitySchemeVersionService.listActiveVersionsByMaterialId(materialId);
        List<StabilitySchemeVersionRespVO> voList = dtoList.stream().map(dto -> {
            StabilitySchemeVersionRespVO vo = BeanUtil.copyProperties(dto, StabilitySchemeVersionRespVO.class);
            if (dto.getMaterialId() != null) {
                StabilitySchemeVersionRespVO.MaterialInfoRespVO material = new StabilitySchemeVersionRespVO.MaterialInfoRespVO();
                material.setMaterialId(dto.getMaterialId());
                material.setMaterialName(dto.getMaterialName());
                material.setMaterialCode(dto.getMaterialCode());
                vo.setMaterial(material);
            }
            return vo;
        }).collect(Collectors.toList());
        return ResponseInfo.success(voList);
    }

    @PutMapping("/version/{id}/activate")
    @ApiOperation("提交稳定性方案版本审批（已完成/已失效 → 审批中）")
    public ResponseInfo<Void> activateVersion(@PathVariable Long id) {
        stabilitySchemeVersionService.activateVersion(id);
        return ResponseInfo.success();
    }

    @PutMapping("/version/{id}/deactivate")
    @ApiOperation("停用稳定性方案版本")
    public ResponseInfo<Void> deactivateVersion(@PathVariable Long id) {
        stabilitySchemeVersionService.deactivateVersion(id);
        return ResponseInfo.success();
    }

    @PutMapping("/version/{id}/void")
    @ApiOperation("作废稳定性方案版本")
    public ResponseInfo<Void> voidVersion(@PathVariable Long id) {
        stabilitySchemeVersionService.voidVersion(id);
        return ResponseInfo.success();
    }

    @PostMapping("/version/{id}/complete")
    @ApiOperation("完成稳定性方案版本编辑（EDITING→COMPLETED）：提交全量数据，后台校验必填项并置为已完成")
    public ResponseInfo<Void> completeVersion(@PathVariable Long id,
                                              @RequestBody @Validated StabilitySchemeCompleteSaveReqVO reqVO) {
        StabilitySchemeSaveDTO basicDTO = BeanUtil.copyProperties(reqVO.getBasic(), StabilitySchemeSaveDTO.class);
        basicDTO.setId(reqVO.getBasic().getSchemeId());
        if (reqVO.getBasic().getMaterial() != null) {
            StabilitySchemeSaveDTO.MaterialInfoDTO materialDTO = new StabilitySchemeSaveDTO.MaterialInfoDTO();
            materialDTO.setMaterialId(reqVO.getBasic().getMaterial().getMaterialId());
            materialDTO.setMaterialName(reqVO.getBasic().getMaterial().getMaterialName());
            materialDTO.setMaterialCode(reqVO.getBasic().getMaterial().getMaterialCode());
            basicDTO.setMaterial(materialDTO);
        }

        StabilitySchemeItemSaveDTO itemSaveDTO = new StabilitySchemeItemSaveDTO();
        itemSaveDTO.setVersionId(id);
        if (reqVO.getItemUpdates() != null) {
            itemSaveDTO.setItems(reqVO.getItemUpdates().stream().map(itemVO -> {
                StabilitySchemeItemSaveDTO.ItemDTO itemDTO = BeanUtil.copyProperties(itemVO,
                        StabilitySchemeItemSaveDTO.ItemDTO.class, "parameters");
                if (itemVO.getInspectionParameters() != null) {
                    itemDTO.setInspectionParameters(BeanUtil.copyToList(itemVO.getInspectionParameters(),
                            StabilitySchemeItemSaveDTO.ParameterDTO.class));
                }
                return itemDTO;
            }).collect(Collectors.toList()));
        }

        // 稳定性计划（稳定性方案特有，对应检验方案的取样配置）
        StabilitySchemePlanSaveDTO planSaveDTO = new StabilitySchemePlanSaveDTO();
        planSaveDTO.setVersionId(id);
        if (reqVO.getPlans() != null) {
            planSaveDTO.setPlans(reqVO.getPlans().stream().map(planVO -> {
                StabilitySchemePlanSaveDTO.PlanDTO planDTO = BeanUtil.copyProperties(planVO,
                        StabilitySchemePlanSaveDTO.PlanDTO.class, "timepoints");
                if (planVO.getTimepoints() != null) {
                    planDTO.setTimepoints(planVO.getTimepoints().stream().map(tpVO -> {
                        StabilitySchemePlanSaveDTO.TimepointDTO tpDTO = BeanUtil.copyProperties(tpVO,
                                StabilitySchemePlanSaveDTO.TimepointDTO.class, "paramRefs");
                        if (tpVO.getParamRefs() != null) {
                            tpDTO.setParamRefs(BeanUtil.copyToList(tpVO.getParamRefs(),
                                    StabilitySchemePlanSaveDTO.ParamRefDTO.class));
                        }
                        return tpDTO;
                    }).collect(Collectors.toList()));
                }
                return planDTO;
            }).collect(Collectors.toList()));
        }

        stabilitySchemeVersionService.completeVersion(id, basicDTO, itemSaveDTO, planSaveDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/version/copy")
    @ApiOperation("复制稳定性方案版本")
    public ResponseInfo<Long> copyVersion(@RequestBody @Validated StabilitySchemeVersionCopyReqVO reqVO) {
        Long newVersionId = stabilitySchemeVersionService.copyVersion(
                reqVO.getSourceVersionId(), reqVO.getNewVersionNo(), reqVO.getDescription());
        return ResponseInfo.success(newVersionId);
    }

    @PostMapping("/copy-from-version")
    @ApiOperation("新增稳定性方案（从现有方案版本复制），返回方案与版本信息")
    public ResponseInfo<StabilitySchemeBasicSaveRespVO> copySchemeFromVersion(
            @RequestBody @Validated StabilitySchemeCopyFromVersionReqVO reqVO) {
        StabilitySchemeCopyFromVersionDTO dto = new StabilitySchemeCopyFromVersionDTO();
        dto.setSourceVersionId(reqVO.getSourceVersionId());
        dto.setNewSchemeName(reqVO.getNewSchemeName());
        dto.setNewSchemeCode(reqVO.getNewSchemeCode());
        dto.setNewVersionNo(reqVO.getNewVersionNo());
        dto.setDescription(reqVO.getDescription());
        dto.setDeptIds(reqVO.getDeptIds());
        if (reqVO.getMaterial() != null) {
            StabilitySchemeCopyFromVersionDTO.MaterialInfoDTO materialDTO = new StabilitySchemeCopyFromVersionDTO.MaterialInfoDTO();
            materialDTO.setMaterialId(reqVO.getMaterial().getMaterialId());
            materialDTO.setMaterialName(reqVO.getMaterial().getMaterialName());
            materialDTO.setMaterialCode(reqVO.getMaterial().getMaterialCode());
            dto.setMaterial(materialDTO);
        }
        StabilitySchemeBasicSaveRespDTO respDTO = stabilitySchemeService.copySchemeFromVersion(dto);
        return ResponseInfo.success(BeanUtil.copyProperties(respDTO, StabilitySchemeBasicSaveRespVO.class));
    }

    @PutMapping("/version/{id}/submit-approval")
    @ApiOperation("提交稳定性方案版本审批（已完成/已失效 → 审批中）")
    public ResponseInfo<Void> submitForApproval(@PathVariable Long id) {
        stabilitySchemeVersionService.activateVersion(id);
        return ResponseInfo.success();
    }

    @GetMapping("/version/audit/page")
    @ApiOperation("审批待办列表")
    public ResponseInfo<CommonPage<StabilitySchemeVersionAuditDTO>> getAuditTodoPage(
            @Validated StabilitySchemeVersionAuditQueryReqVO reqVO) {
        StabilitySchemeVersionAuditQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilitySchemeVersionAuditQueryDTO.class);
        return ResponseInfo.success(stabilitySchemeVersionService.pageAuditVersions(queryDTO));
    }

    @PostMapping("/version/save-items")
    @ApiOperation("保存稳定性方案版本检验项目-分析项（前端平铺提交，后台按检验项目分组覆盖保存）")
    public ResponseInfo<Void> saveVersionItems(@RequestBody @Validated StabilitySchemeVersionSaveItemsReqVO reqVO) {
        List<StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO> parameters = null;
        if (reqVO.getParameters() != null) {
            parameters = reqVO.getParameters().stream().map(p -> {
                StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO item =
                        new StabilitySchemeVersionSaveItemsDTO.ParameterItemDTO();
                item.setInspectItemId(p.getInspectItemId());
                item.setDuration(p.getDuration());
                item.setTimeUnit(p.getTimeUnit());
                item.setTeams(p.getTeams());
                item.setParameterId(p.getParameterId());
                item.setStandardRule(p.getStandardRule());
                item.setIsExecutable(p.getIsExecutable());
                item.setIsReportable(p.getIsReportable());
                item.setExecuteMethod(p.getExecuteMethod());
                item.setFinalExpression(p.getFinalExpression());
                item.setRecordId(p.getRecordId());
                item.setRecordCode(p.getRecordCode());
                item.setRecordVersionId(p.getRecordVersionId());
                item.setRecordItemId(p.getRecordItemId());
                return item;
            }).collect(Collectors.toList());
        }
        stabilitySchemeItemService.mergeVersionItems(reqVO.getVersionId(), parameters);
        return ResponseInfo.success();
    }

    @GetMapping("/version/{versionId}/items")
    @ApiOperation("查询稳定性方案版本下的检验项目配置列表")
    public ResponseInfo<List<StabilitySchemeItemRespVO>> listItems(@PathVariable Long versionId) {
        List<StabilitySchemeItemDTO> dtoList = stabilitySchemeItemService.listItems(versionId);
        return ResponseInfo.success(toItemRespVOList(dtoList));
    }

    @DeleteMapping("/version/item/{itemId}")
    @ApiOperation("删除稳定性方案检验项目配置")
    public ResponseInfo<Void> deleteItem(@PathVariable Long itemId) {
        stabilitySchemeItemService.deleteItem(itemId);
        return ResponseInfo.success();
    }

    @DeleteMapping("/version/parameter/{parameterId}")
    @ApiOperation("删除稳定性方案分析项配置")
    public ResponseInfo<Void> deleteParameter(@PathVariable Long parameterId) {
        stabilitySchemeItemService.deleteParameter(parameterId);
        return ResponseInfo.success();
    }

    @GetMapping("/version/parameter/{parameterConfigId}")
    @ApiOperation("查询稳定性方案分析项配置详情（含数据点和判定）")
    public ResponseInfo<StabilitySchemeParameterRespVO> getParameterDetail(@PathVariable Long parameterConfigId) {
        StabilitySchemeParameterDTO dto = stabilitySchemeItemService.getParameterDetail(parameterConfigId);
        if (dto == null) return ResponseInfo.success(null);
        StabilitySchemeParameterRespVO pvo = new StabilitySchemeParameterRespVO();
        pvo.setParameterConfigId(dto.getId());
        pvo.setSchemeId(dto.getSchemeId());
        pvo.setVersionId(dto.getVersionId());
        pvo.setItemConfigId(dto.getItemConfigId());
        pvo.setInspectItemId(dto.getInspectItemId());
        pvo.setParameterId(dto.getParameterId());
        pvo.setParameterName(dto.getParameterName());
        pvo.setParameterCode(dto.getParameterCode());
        pvo.setStandardRule(dto.getStandardRule());
        pvo.setIsExecutable(dto.getIsExecutable());
        pvo.setIsReportable(dto.getIsReportable());
        pvo.setExecuteMethod(dto.getExecuteMethod());
        pvo.setFinalExpression(dto.getFinalExpression());
        pvo.setRecordId(dto.getRecordId());
        pvo.setRecordCode(dto.getRecordCode());
        pvo.setRecordVersionId(dto.getRecordVersionId());
        pvo.setRecordItemId(dto.getRecordItemId());
        pvo.setRecordName(dto.getRecordName());
        pvo.setRecordVersion(dto.getRecordVersion());
        pvo.setJudgmentConfigError(dto.getJudgmentConfigError());
        pvo.setJudgmentDataPointDeleted(dto.getJudgmentDataPointDeleted());
        pvo.setJudgmentDataPointBindingMissing(dto.getJudgmentDataPointBindingMissing());
        pvo.setJudgmentDataPointTypeChanged(dto.getJudgmentDataPointTypeChanged());
        pvo.setJudgmentDataPointOptionInvalid(dto.getJudgmentDataPointOptionInvalid());
        if (dto.getDataPoints() != null) {
            pvo.setDataPoints(dto.getDataPoints().stream().map(dp -> {
                StabilitySchemeDataPointRespVO dpvo = new StabilitySchemeDataPointRespVO();
                dpvo.setDataPointConfigId(dp.getId());
                dpvo.setSchemeId(dp.getSchemeId());
                dpvo.setVersionId(dp.getVersionId());
                dpvo.setItemConfigId(dp.getItemConfigId());
                dpvo.setParameterConfigId(dp.getParameterConfigId());
                dpvo.setParameterId(dp.getParameterId());
                dpvo.setDataPointId(dp.getDataPointId());
                dpvo.setName(dp.getName());
                dpvo.setPointType(dp.getPointType());
                dpvo.setTrendLineConfig(dp.getTrendLineConfig());
                dpvo.setOptions(dp.getOptions());
                dpvo.setTimeFormat(dp.getTimeFormat());
                dpvo.setDateStyle(dp.getDateStyle());
                dpvo.setRoundingUp(dp.getRoundingUp());
                dpvo.setReportDisplay(dp.getReportDisplay());
                dpvo.setRecordId(dp.getRecordId());
                dpvo.setRecordVersionId(dp.getRecordVersionId());
                dpvo.setComponentId(dp.getComponentId());
                dpvo.setRecordItemId(dp.getRecordItemId());
                dpvo.setFieldId(dp.getFieldId());
                dpvo.setReferencedByJudgment(dp.getReferencedByJudgment());
                return dpvo;
            }).collect(Collectors.toList()));
        }
        if (dto.getJudgments() != null) {
            pvo.setJudgments(dto.getJudgments().stream().map(j -> {
                StabilitySchemeJudgmentRespVO jvo = new StabilitySchemeJudgmentRespVO();
                jvo.setJudgmentConfigId(j.getId());
                jvo.setSchemeId(j.getSchemeId());
                jvo.setVersionId(j.getVersionId());
                jvo.setItemConfigId(j.getItemConfigId());
                jvo.setParameterConfigId(j.getParameterConfigId());
                jvo.setDataPointConfigId(j.getDataPointConfigId());
                jvo.setParameterId(j.getParameterId());
                jvo.setDataPointId(j.getDataPointId());
                jvo.setJudgementConfigName(j.getJudgementConfigName());
                jvo.setPointType(j.getPointType());
                jvo.setJudgmentType(j.getJudgmentType());
                jvo.setDefaultResult(j.getDefaultResult());
                jvo.setMinValue(j.getMinValue());
                jvo.setMinOperator(j.getMinOperator());
                jvo.setMaxValue(j.getMaxValue());
                jvo.setMaxOperator(j.getMaxOperator());
                jvo.setStandardValue(j.getStandardValue());
                jvo.setExpression(j.getExpression());
                jvo.setMinTime(j.getMinTime());
                jvo.setMaxTime(j.getMaxTime());
                jvo.setDataPointDeleted(j.getDataPointDeleted());
                jvo.setDataPointBindingMissing(j.getDataPointBindingMissing());
                jvo.setDataPointTypeChanged(j.getDataPointTypeChanged());
                jvo.setDataPointOptionInvalid(j.getDataPointOptionInvalid());
                return jvo;
            }).collect(Collectors.toList()));
        }
        return ResponseInfo.success(pvo);
    }

    @PutMapping("/version/parameter/execute-method")
    @ApiOperation("更新稳定性方案分析项执行方式（ELN需同时更新记录ID/版本ID/编码）")
    public ResponseInfo<Void> updateParameterExecuteMethod(
            @RequestBody @Validated StabilitySchemeParameterExecuteMethodUpdateReqVO reqVO) {
        stabilitySchemeItemService.updateExecuteMethod(
                reqVO.getParameterConfigId(), reqVO.getExecuteMethod(),
                reqVO.getRecordId(), reqVO.getRecordVersionId(),
                reqVO.getRecordCode(), reqVO.getRecordItemId());
        return ResponseInfo.success();
    }

    @DeleteMapping("/version/data-point/{dataPointId}")
    @ApiOperation("删除稳定性方案数据点配置（级联删除引用该数据点的判定）")
    public ResponseInfo<Void> deleteDataPoint(@PathVariable Long dataPointId) {
        stabilitySchemeItemService.deleteDataPoint(dataPointId);
        return ResponseInfo.success();
    }

    @DeleteMapping("/version/judgment/{judgmentId}")
    @ApiOperation("删除稳定性方案判定配置")
    public ResponseInfo<Void> deleteJudgment(@PathVariable Long judgmentId) {
        stabilitySchemeItemService.deleteJudgment(judgmentId);
        return ResponseInfo.success();
    }

    @PostMapping("/version/items/update-data-points")
    @ApiOperation("批量更新数据点配置（增量：无ID则新增，有ID则更新，不在列表中则删除）")
    public ResponseInfo<Void> updateDataPoints(@RequestBody @Validated List<StabilitySchemeDataPointBatchUpdateReqVO> reqVO) {
        if (CollectionUtil.isEmpty(reqVO)) {
            throw new BmosException(LimsResponseCode.STABILITY_SCHEME_DATA_POINT_EMPTY);
        }
        List<StabilitySchemeDataPointBatchUpdateDTO> dtos = BeanUtil.copyToList(reqVO, StabilitySchemeDataPointBatchUpdateDTO.class);
        stabilitySchemeItemService.updateDataPoints(dtos);
        return ResponseInfo.success();
    }

    @PostMapping("/version/items/update-judgments")
    @ApiOperation("批量更新判定配置（增量：无ID则新增，有ID则更新，不在列表中则删除）")
    public ResponseInfo<Void> updateJudgments(@RequestBody @Validated StabilitySchemeJudgmentBatchUpdateReqVO reqVO) {
        List<StabilitySchemeJudgmentBatchUpdateDTO> dtos = reqVO.getUpdateJudgmentList() == null
                ? java.util.Collections.emptyList()
                : BeanUtil.copyToList(reqVO.getUpdateJudgmentList(), StabilitySchemeJudgmentBatchUpdateDTO.class);
        stabilitySchemeItemService.updateJudgments(reqVO.getParameterConfigId(), reqVO.getFinalExpression(), dtos);
        return ResponseInfo.success();
    }

    // ===================== 检验计划 =====================

    @PostMapping("/version/plans/save")
    @ApiOperation("保存稳定性方案检验计划（增量更新）")
    public ResponseInfo<Void> savePlans(@RequestBody @Validated StabilitySchemePlanSaveReqVO reqVO) {
        StabilitySchemePlanSaveDTO saveDTO = new StabilitySchemePlanSaveDTO();
        saveDTO.setVersionId(reqVO.getVersionId());
        if (reqVO.getPlans() != null) {
            saveDTO.setPlans(reqVO.getPlans().stream().map(planVO -> {
                StabilitySchemePlanSaveDTO.PlanDTO planDTO = BeanUtil.copyProperties(planVO,
                        StabilitySchemePlanSaveDTO.PlanDTO.class, "timepoints");
                if (planVO.getTimepoints() != null) {
                    planDTO.setTimepoints(planVO.getTimepoints().stream().map(tpVO -> {
                        StabilitySchemePlanSaveDTO.TimepointDTO tpDTO = BeanUtil.copyProperties(tpVO,
                                StabilitySchemePlanSaveDTO.TimepointDTO.class, "paramRefs");
                        if (tpVO.getParamRefs() != null) {
                            tpDTO.setParamRefs(BeanUtil.copyToList(tpVO.getParamRefs(),
                                    StabilitySchemePlanSaveDTO.ParamRefDTO.class));
                        }
                        return tpDTO;
                    }).collect(Collectors.toList()));
                }
                return planDTO;
            }).collect(Collectors.toList()));
        }
        stabilitySchemePlanService.savePlans(saveDTO);
        return ResponseInfo.success();
    }

    @GetMapping("/version/{versionId}/plans")
    @ApiOperation("查询稳定性方案版本下的检验计划列表")
    public ResponseInfo<List<StabilitySchemePlanRespVO>> listPlans(@PathVariable Long versionId) {
        List<StabilitySchemePlanDTO> dtoList = stabilitySchemePlanService.listPlans(versionId);
        return ResponseInfo.success(BeanUtil.copyToList(dtoList, StabilitySchemePlanRespVO.class));
    }

    @DeleteMapping("/version/plan/{planId}")
    @ApiOperation("删除稳定性方案检验计划")
    public ResponseInfo<Void> deletePlan(@PathVariable Long planId) {
        stabilitySchemePlanService.deletePlan(planId);
        return ResponseInfo.success();
    }

    @DeleteMapping("/version/plan/timepoint/{timepointId}")
    @ApiOperation("删除稳定性方案时间点")
    public ResponseInfo<Void> deleteTimepoint(@PathVariable Long timepointId) {
        stabilitySchemePlanService.deleteTimepoint(timepointId);
        return ResponseInfo.success();
    }

    // ===================== 数据权限 =====================

    @PutMapping("/{id}/permissions")
    @ApiOperation("保存稳定性方案数据权限（部门）")
    public ResponseInfo<Void> savePermissions(@PathVariable Long id,
                                              @RequestBody StabilitySchemePermissionReqVO reqVO) {
        stabilitySchemeService.savePermissions(id, reqVO.getDeptIds());
        return ResponseInfo.success();
    }

    @GetMapping("/{id}/permissions")
    @ApiOperation("查询稳定性方案数据权限（部门ID列表）")
    public ResponseInfo<List<Long>> getPermissions(@PathVariable Long id) {
        return ResponseInfo.success(stabilitySchemeService.getPermissions(id));
    }

    // ===================== 私有映射方法 =====================

    private List<StabilitySchemeItemRespVO> toItemRespVOList(List<StabilitySchemeItemDTO> dtoList) {
        if (dtoList == null) return new ArrayList<>();
        return dtoList.stream().map(this::toItemRespVO).collect(Collectors.toList());
    }

    private StabilitySchemeItemRespVO toItemRespVO(StabilitySchemeItemDTO dto) {
        StabilitySchemeItemRespVO vo = new StabilitySchemeItemRespVO();
        vo.setItemConfigId(dto.getId());
        vo.setSchemeId(dto.getSchemeId());
        vo.setVersionId(dto.getVersionId());
        vo.setInspectItemId(dto.getInspectItemId());
        vo.setInspectItemName(dto.getInspectItemName());
        vo.setInspectItemCode(dto.getInspectItemCode());
        vo.setDuration(dto.getDuration());
        vo.setTimeUnit(dto.getTimeUnit());
        vo.setTeams(dto.getTeams());
        vo.setIsRequired(dto.getIsRequired());
        vo.setRemark(dto.getRemark());
        if (dto.getInspectionParameters() != null) {
            vo.setInspectionParameters(dto.getInspectionParameters().stream().map(p -> {
                StabilitySchemeParameterRespVO pvo = new StabilitySchemeParameterRespVO();
                pvo.setParameterConfigId(p.getId());
                pvo.setSchemeId(p.getSchemeId());
                pvo.setVersionId(p.getVersionId());
                pvo.setItemConfigId(p.getItemConfigId());
                pvo.setInspectItemId(p.getInspectItemId());
                pvo.setParameterId(p.getParameterId());
                pvo.setParameterName(p.getParameterName());
                pvo.setParameterCode(p.getParameterCode());
                pvo.setStandardRule(p.getStandardRule());
                pvo.setIsExecutable(p.getIsExecutable());
                pvo.setIsReportable(p.getIsReportable());
                pvo.setExecuteMethod(p.getExecuteMethod());
                pvo.setFinalExpression(p.getFinalExpression());
                pvo.setRecordId(p.getRecordId());
                pvo.setRecordCode(p.getRecordCode());
                pvo.setRecordVersionId(p.getRecordVersionId());
                pvo.setRecordItemId(p.getRecordItemId());
                pvo.setRecordName(p.getRecordName());
                pvo.setRecordVersion(p.getRecordVersion());
                if (p.getDataPoints() != null) {
                    pvo.setDataPoints(p.getDataPoints().stream().map(dp -> {
                        StabilitySchemeDataPointRespVO dpvo = new StabilitySchemeDataPointRespVO();
                        dpvo.setDataPointConfigId(dp.getId());
                        dpvo.setSchemeId(dp.getSchemeId());
                        dpvo.setVersionId(dp.getVersionId());
                        dpvo.setItemConfigId(dp.getItemConfigId());
                        dpvo.setParameterConfigId(dp.getParameterConfigId());
                        dpvo.setParameterId(dp.getParameterId());
                        dpvo.setDataPointId(dp.getDataPointId());
                        dpvo.setName(dp.getName());
                        dpvo.setPointType(dp.getPointType());
                        dpvo.setTrendLineConfig(dp.getTrendLineConfig());
                        dpvo.setOptions(dp.getOptions());
                        dpvo.setTimeFormat(dp.getTimeFormat());
                        dpvo.setDateStyle(dp.getDateStyle());
                        dpvo.setRoundingUp(dp.getRoundingUp());
                        dpvo.setReportDisplay(dp.getReportDisplay());
                        dpvo.setRecordId(dp.getRecordId());
                        dpvo.setRecordVersionId(dp.getRecordVersionId());
                        dpvo.setComponentId(dp.getComponentId());
                        dpvo.setRecordItemId(dp.getRecordItemId());
                        dpvo.setFieldId(dp.getFieldId());
                        dpvo.setReferencedByJudgment(dp.getReferencedByJudgment());
                        return dpvo;
                    }).collect(Collectors.toList()));
                }
                if (p.getJudgments() != null) {
                    pvo.setJudgments(p.getJudgments().stream().map(j -> {
                        StabilitySchemeJudgmentRespVO jvo = new StabilitySchemeJudgmentRespVO();
                        jvo.setJudgmentConfigId(j.getId());
                        jvo.setSchemeId(j.getSchemeId());
                        jvo.setVersionId(j.getVersionId());
                        jvo.setItemConfigId(j.getItemConfigId());
                        jvo.setParameterConfigId(j.getParameterConfigId());
                        jvo.setDataPointConfigId(j.getDataPointConfigId());
                        jvo.setParameterId(j.getParameterId());
                        jvo.setDataPointId(j.getDataPointId());
                        jvo.setJudgementConfigName(j.getJudgementConfigName());
                        jvo.setPointType(j.getPointType());
                        jvo.setJudgmentType(j.getJudgmentType());
                        jvo.setDefaultResult(j.getDefaultResult());
                        jvo.setMinValue(j.getMinValue());
                        jvo.setMinOperator(j.getMinOperator());
                        jvo.setMaxValue(j.getMaxValue());
                        jvo.setMaxOperator(j.getMaxOperator());
                        jvo.setStandardValue(j.getStandardValue());
                        jvo.setExpression(j.getExpression());
                        jvo.setMinTime(j.getMinTime());
                        jvo.setMaxTime(j.getMaxTime());
                        jvo.setDataPointDeleted(j.getDataPointDeleted());
                        jvo.setDataPointBindingMissing(j.getDataPointBindingMissing());
                        jvo.setDataPointTypeChanged(j.getDataPointTypeChanged());
                        jvo.setDataPointOptionInvalid(j.getDataPointOptionInvalid());
                        return jvo;
                    }).collect(Collectors.toList()));
                }
                pvo.setJudgmentConfigError(p.getJudgmentConfigError());
                pvo.setJudgmentDataPointDeleted(p.getJudgmentDataPointDeleted());
                pvo.setJudgmentDataPointBindingMissing(p.getJudgmentDataPointBindingMissing());
                pvo.setJudgmentDataPointTypeChanged(p.getJudgmentDataPointTypeChanged());
                pvo.setJudgmentDataPointOptionInvalid(p.getJudgmentDataPointOptionInvalid());
                return pvo;
            }).collect(Collectors.toList()));
        }
        return vo;
    }
}
