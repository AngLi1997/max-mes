package com.bmos.lims2.web.inspect.scheme;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
// removed unused import: InspectionSchemeSaveDTO
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeParameterDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeCopyFromVersionDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.*;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeBasicSaveRespDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.ReportDataPointRespDTO;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeDataPointService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeJudgmentService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeParameterService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeItemService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeReportService;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeService;
import com.bmos.lims2.web.inspect.scheme.vo.request.*;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeVersionFullConfigRespVO.AnalyzeItemConfigRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeVersionFullConfigRespVO.DataPointConfigRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.InspectionSchemeVersionFullConfigRespVO.JudgmentConfigRespVO;
import com.bmos.lims2.web.inspect.scheme.vo.response.ReportDataPointRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 检验方案Controller
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@RestController
@RequestMapping("/inspection-scheme")
@Api(tags = "检验方案-接口")
@Validated
public class InspectionSchemeController {

    @Autowired
    private InspectionSchemeService inspectionSchemeService;

    @Autowired
    private InspectionSchemeDataPointService inspectionSchemeDataPointService;

    @Autowired
    private InspectionSchemeJudgmentService inspectionSchemeJudgmentService;

    @Autowired
    private InspectionSchemeParameterService inspectionSchemeParameterService;

    @Autowired
    private InspectionSchemeReportService inspectionSchemeReportService;

    @Autowired
    private InspectionSchemeItemService inspectionSchemeItemService;

    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

    @PostMapping("/save")
    @ApiOperation("保存检验方案（融合save-basic、update-items、update-samplings）")
    public ResponseInfo<InspectionSchemeBasicSaveRespDTO> saveInspectionScheme(@RequestBody @Validated InspectionSchemeSaveFusionReqVO reqVO) {
        InspectionSchemeSaveFusionDTO fusionDTO = new InspectionSchemeSaveFusionDTO();
        fusionDTO.setBasic(BeanUtil.copyProperties(reqVO.getBasic(), InspectionSchemeBasicSaveDTO.class));
        if (reqVO.getItemUpdates() != null) {
            fusionDTO.setItemUpdates(cn.hutool.core.bean.BeanUtil.copyToList(reqVO.getItemUpdates(), InspectionSchemeItemUpdateDTO.class));
        }
        if (reqVO.getSamplingUpdates() != null) {
            fusionDTO.setSamplingUpdates(cn.hutool.core.bean.BeanUtil.copyToList(reqVO.getSamplingUpdates(), InspectionSchemeSamplingUpdateDTO.class));
        }
        return ResponseInfo.success(inspectionSchemeService.saveInspectionSchemeFusion(fusionDTO));
    }

    @PostMapping("/save-basic")
    @ApiOperation("保存检验方案基础信息（分步骤保存第一步）")
    public ResponseInfo<InspectionSchemeBasicSaveRespDTO> saveInspectionSchemeBasic(@RequestBody @Validated InspectionSchemeBasicSaveReqVO reqVO) {
        InspectionSchemeBasicSaveDTO saveDTO = BeanUtil.copyProperties(reqVO, InspectionSchemeBasicSaveDTO.class);
        return ResponseInfo.success(inspectionSchemeService.saveInspectionSchemeBasic(saveDTO));
    }

    @GetMapping("/report/data-points/page")
    @ApiOperation("分页查询报告数据点（生效版本、报告项、报告显示）")
    public ResponseInfo<CommonPage<ReportDataPointRespVO>> pageReportDataPoints(@Validated ReportDataPointPageReqVO reqVO) {
        ReportDataPointPageReqDTO reqDTO = BeanUtil.copyProperties(reqVO, ReportDataPointPageReqDTO.class);
        CommonPage<ReportDataPointRespDTO> page = inspectionSchemeReportService.pageReportDataPoints(reqDTO);
        CommonPage<com.bmos.lims2.web.inspect.scheme.vo.response.ReportDataPointRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), ReportDataPointRespVO.class));
        return ResponseInfo.success(respPage);
    }

    @PutMapping("/parameter/execute-method")
    @ApiOperation("更新方案分析项执行方式（ELN需同时更新记录ID/版本ID/编码）")
    public ResponseInfo<Void> updateParameterExecuteMethod(@RequestBody @Validated InspectionSchemeParameterExecuteMethodUpdateReqVO reqVO) {
        inspectionSchemeParameterService.updateExecuteMethod(
                reqVO.getParameterConfigId(),
                reqVO.getExecuteMethod(),
                reqVO.getRecordId(),
                reqVO.getRecordVersionId(),
                reqVO.getRecordCode(),
                reqVO.getRecordItemId()
        );
        return ResponseInfo.success();
    }

    @DeleteMapping("/item/{itemConfigId}")
    @ApiOperation("删除方案检验项目配置（编辑状态下可用；同时级联删除其下所有分析项）")
    public ResponseInfo<Void> deleteSchemeItem(@PathVariable Long itemConfigId) {
        inspectionSchemeItemService.deleteSchemeItem(itemConfigId);
        return ResponseInfo.success();
    }

    @DeleteMapping("/parameter/{parameterConfigId}")
    @ApiOperation("删除方案分析项配置（编辑状态下可用；若检验项目下无分析项则同时删除检验项目）")
    public ResponseInfo<Void> deleteSchemeParameter(@PathVariable Long parameterConfigId) {
        inspectionSchemeItemService.deleteSchemeParameter(parameterConfigId);
        return ResponseInfo.success();
    }

    @PostMapping("/version/save-items")
    @ApiOperation("保存方案版本检验项目-分析项（前端选择后传入，后台按检验项目去重分组覆盖保存）")
    public ResponseInfo<Void> saveVersionItems(@RequestBody @Validated InspectionSchemeVersionSaveItemsReqVO reqVO) {
        com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionSaveItemsDTO dto =
                new com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionSaveItemsDTO();
        dto.setSchemeId(reqVO.getSchemeId());
        dto.setVersionId(reqVO.getVersionId());
        if (reqVO.getParameters() != null) {
            dto.setParameters(reqVO.getParameters().stream().map(p -> {
                com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO item =
                        new com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeVersionSaveItemsDTO.ParameterItemDTO();
                item.setInspectItemId(p.getInspectItemId());
                item.setParameterId(p.getParameterId());
                return item;
            }).collect(Collectors.toList()));
        }
        inspectionSchemeService.saveVersionItems(dto);
        return ResponseInfo.success();
    }
    @PostMapping("/update-items")
    @ApiOperation("更新检验项目配置（增量更新）")
    public ResponseInfo<Void> updateInspectionSchemeItems(@RequestBody @Validated List<InspectionSchemeItemUpdateReqVO> reqVO) {
        List<InspectionSchemeItemUpdateDTO> updateDTO = BeanUtil.copyToList(reqVO, InspectionSchemeItemUpdateDTO.class);
        inspectionSchemeService.updateInspectionSchemeItems(updateDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/update-data-points")
    @ApiOperation("批量更新检验方案数据点配置（增量更新）")
    public ResponseInfo<Void> batchUpdateInspectionSchemeDataPoints(@RequestBody @Validated List<InspectionSchemeDataPointBatchUpdateReqVO> reqVO) {
        if (CollectionUtil.isEmpty(reqVO)){
            throw new BmosException(LimsResponseCode.INSPECTION_SCHEME_DATA_POINT_EMPTY);
        }
        List<InspectionSchemeDataPointBatchUpdateDTO> inspectionSchemeDataPointBatchUpdateDTOS = BeanUtil.copyToList(reqVO, InspectionSchemeDataPointBatchUpdateDTO.class);
        inspectionSchemeDataPointService.batchUpdateInspectionSchemeDataPoints(inspectionSchemeDataPointBatchUpdateDTOS);
        return ResponseInfo.success();
    }

    @PostMapping("/data-points/bindings/save")
    @ApiOperation("批量保存数据点与记录字段（ELN）的绑定关系")
    public ResponseInfo<Void> saveDataPointBindings(@RequestBody @Validated List<InspectionSchemeDataPointBindingUpdateReqVO> reqVO) {
        List<InspectionSchemeDataPointBindingUpdateDTO> dtos = BeanUtil.copyToList(reqVO, InspectionSchemeDataPointBindingUpdateDTO.class);
        inspectionSchemeDataPointService.saveDataPointBindings(dtos);
        return ResponseInfo.success();
    }

    @PostMapping("/update-judgments")
    @ApiOperation("批量更新检验方案判定条件配置（增量更新）")
    public ResponseInfo<Void> batchUpdateInspectionSchemeJudgments(@RequestBody @Validated InspectionSchemeParameterJudgmentUpdateVO reqVO) {
        List<InspectionSchemeJudgmentBatchUpdateDTO> inspectionSchemeJudgmentBatchUpdateDTOS =
                reqVO.getUpdateJudgmentList() == null
                        ? java.util.Collections.emptyList()
                        : BeanUtil.copyToList(reqVO.getUpdateJudgmentList(), InspectionSchemeJudgmentBatchUpdateDTO.class);
        inspectionSchemeJudgmentService.batchUpdateInspectionSchemeJudgments(
                reqVO.getParameterConfigId(),
                reqVO.getFinalExpression(),
                inspectionSchemeJudgmentBatchUpdateDTOS
        );
        return ResponseInfo.success();
    }

    @PostMapping("/update-samplings")
    @ApiOperation("更新取样配置（增量更新）")
    public ResponseInfo<Void> updateInspectionSchemeSamplings(@RequestBody @Validated List<InspectionSchemeSamplingUpdateReqVO> reqVO) {
        List<InspectionSchemeSamplingUpdateDTO> updateDTO = BeanUtil.copyToList(reqVO, InspectionSchemeSamplingUpdateDTO.class);
        inspectionSchemeService.updateInspectionSchemeSamplings(updateDTO);
        return ResponseInfo.success();
    }

    @PostMapping("/page")
    @ApiOperation("分页查询检验方案")
    public ResponseInfo<CommonPage<InspectionSchemeRespVO>> pageInspectionScheme(@RequestBody @Validated InspectionSchemeQueryReqVO reqVO) {
        InspectionSchemeQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, InspectionSchemeQueryDTO.class);
        // 解析物料ID集合：优先 materialId，其次 categoryId(取分类下启用检品)
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
        // 若按分类未解析到任何启用检品，直接返回空分页
        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<InspectionSchemeRespVO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }
        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }
        CommonPage<InspectionSchemeDTO> page = inspectionSchemeService.pageInspectionScheme(queryDTO);
        CommonPage<InspectionSchemeRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), InspectionSchemeRespVO.class));
        return ResponseInfo.success(respPage);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取检验方案详情")
    public ResponseInfo<InspectionSchemeRespVO> getInspectionScheme(@PathVariable Long id) {
        InspectionSchemeDTO dto = inspectionSchemeService.getInspectionScheme(id);
        return ResponseInfo.success(BeanUtil.copyProperties(dto, InspectionSchemeRespVO.class));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除检验方案")
    public ResponseInfo<Void> deleteInspectionScheme(@PathVariable Long id) {
        inspectionSchemeService.deleteInspectionScheme(id);
        return ResponseInfo.success();
    }

    @GetMapping("/parameter/{parameterConfigId}")
    @ApiOperation("通过分析项配置ID查询分析项的详细配置信息")
    public ResponseInfo<AnalyzeItemConfigRespVO> getInspectionSchemeParameterDetail(@PathVariable Long parameterConfigId) {
        InspectionSchemeParameterDTO dto = inspectionSchemeParameterService.getInspectionSchemeParameterDetail(parameterConfigId);
        if (dto == null) {
            return ResponseInfo.success(null);
        }

        AnalyzeItemConfigRespVO respVO = new AnalyzeItemConfigRespVO();
        // 对齐字段，与完整配置返回保持一致
        respVO.setParameterConfigId(dto.getId());
        respVO.setSchemeId(dto.getSchemeId());
        respVO.setVersionId(dto.getVersionId());
        respVO.setPackageId(dto.getPackageId());
        respVO.setInspectItemId(dto.getInspectItemId());
        respVO.setItemConfigId(dto.getItemConfigId());
        respVO.setParameterId(dto.getParameterId());
        respVO.setParameterName(dto.getParameterName());
        respVO.setParameterCode(dto.getParameterCode());
        respVO.setStandardRule(dto.getStandardRule());
        respVO.setRecordId(dto.getRecordId());
        respVO.setRecordItemId(dto.getRecordItemId());
        respVO.setIsReportable(dto.getIsReportable());
        respVO.setIsExecutable(dto.getIsExecutable());
        respVO.setFinalExpression(dto.getFinalExpression());
        respVO.setExecuteMethod(dto.getExecuteMethod());
        respVO.setRecordId(dto.getRecordId());
        respVO.setRecordName(dto.getRecordName());
        respVO.setRecordCode(dto.getRecordCode());
        respVO.setRecordVersionId(dto.getRecordVersionId());
        respVO.setRecordVersion(dto.getRecordVersion());

        if (dto.getDataPoints() != null) {
            List<DataPointConfigRespVO> dpList = dto.getDataPoints().stream().map(dp -> {
                DataPointConfigRespVO vo = new DataPointConfigRespVO();
                vo.setDataPointConfigId(dp.getId());
                vo.setDataPointId(dp.getDataPointId());
                vo.setParameterConfigId(dto.getId());
                vo.setParameterId(dto.getParameterId());
                vo.setSchemeId(dp.getSchemeId());
                vo.setVersionId(dp.getVersionId());
                vo.setPackageId(dp.getPackageId());
                vo.setInspectItemId(dp.getInspectItemId());
                vo.setName(dp.getName());
                vo.setPointType(dp.getPointType());
                vo.setTimeFormat(dp.getTimeFormat());
                vo.setDateStyle(dp.getDateStyle());
                vo.setRoundingUp(dp.getRoundingUp());
                vo.setTrendLineConfig(dp.getTrendLineConfig());
                vo.setOptions(dp.getOptions());
                vo.setReportDisplay(dp.getReportDisplay());
                vo.setReferencedByJudgment(dp.getReferencedByJudgment());
                vo.setRecordId(dp.getRecordId());
                vo.setRecordVersionId(dp.getRecordVersionId());
                vo.setFieldId(dp.getFieldId());
                vo.setRecordItemId(dp.getRecordItemId());
                vo.setComponentId(dp.getComponentId());
                return vo;
            }).collect(Collectors.toList());
            respVO.setDataPoints(dpList);
        }

        if (dto.getJudgments() != null) {
            List<JudgmentConfigRespVO> jList = dto.getJudgments().stream().map(j -> {
                JudgmentConfigRespVO vo = new JudgmentConfigRespVO();
                vo.setJudgmentConfigId(j.getId());
                vo.setSchemeId(j.getSchemeId());
                vo.setVersionId(j.getVersionId());
                vo.setPackageId(j.getPackageId());
                vo.setInspectItemId(j.getInspectItemId());
                vo.setParameterId(j.getParameterId());
                vo.setParameterConfigId(j.getParameterConfigId());
                vo.setDataPointConfigId(j.getDataPointConfigId());
                vo.setDataPointId(j.getDataPointId());
                vo.setJudgmentType(j.getJudgmentType());
                vo.setDefaultResult(j.getDefaultResult());
                vo.setMinValue(j.getMinValue());
                vo.setMaxValue(j.getMaxValue());
                vo.setStandardValue(j.getStandardValue());
                vo.setExpression(j.getExpression());
                vo.setJudgementConfigName(j.getJudgementConfigName());
                vo.setMinOperator(j.getMinOperator());
                vo.setMaxOperator(j.getMaxOperator());
                vo.setMinTime(j.getMinTime());
                vo.setMaxTime(j.getMaxTime());
                vo.setDataPointDeleted(j.getDataPointDeleted());
                vo.setDataPointBindingMissing(j.getDataPointBindingMissing());
                vo.setDataPointTypeChanged(j.getDataPointTypeChanged());
                vo.setDataPointOptionInvalid(j.getDataPointOptionInvalid());
                return vo;
            }).collect(Collectors.toList());
            respVO.setJudgments(jList);
        }

        // 计算判定条件引用的数据点异常：删除 / 类型变更 / 绑定缺失 / 选项缺失（与版本详情保持字段一致）
        if (dto.getJudgments() != null && !dto.getJudgments().isEmpty()) {
            respVO.setJudgmentConfigError(dto.getJudgmentConfigError());
            respVO.setJudgmentDataPointDeleted(dto.getJudgmentDataPointDeleted());
            respVO.setJudgmentDataPointTypeChanged(dto.getJudgmentDataPointTypeChanged());
            respVO.setJudgmentDataPointBindingMissing(dto.getJudgmentDataPointBindingMissing());
            respVO.setJudgmentDataPointOptionInvalid(dto.getJudgmentDataPointOptionInvalid());
        } else {
            respVO.setJudgmentConfigError(false);
            respVO.setJudgmentDataPointDeleted(false);
            respVO.setJudgmentDataPointTypeChanged(false);
            respVO.setJudgmentDataPointBindingMissing(false);
            respVO.setJudgmentDataPointOptionInvalid(false);
        }

        return ResponseInfo.success(respVO);
    }

    @GetMapping("/dropdown/material/{materialId}")
    @ApiOperation("通过检品ID查询检验方案下拉数据")
    public ResponseInfo<List<InspectionSchemeDropdownDTO>> getInspectionSchemeDropdownByMaterialId(@PathVariable Long materialId) {
        List<InspectionSchemeDropdownDTO> dropdownList = inspectionSchemeService.getInspectionSchemeDropdownByMaterialId(materialId);
        return ResponseInfo.success(dropdownList);
    }

    @GetMapping("/dropdown/material/{materialId}/versions")
    @ApiOperation("通过检品ID查询可选的方案生效版本下拉（仅返回版本ID与名称）")
    public ResponseInfo<List<com.bmos.lims2.server.inspect.scheme.dto.response.InspectionSchemeDropdownDTO>> getActiveSchemeVersionsByMaterialId(
            @PathVariable Long materialId) {
        // 复用已有下拉，前端可直接使用 activeVersionId/activeVersionNo
        List<InspectionSchemeDropdownDTO> dropdownList = inspectionSchemeService.getInspectionSchemeDropdownByMaterialId(materialId);
        return ResponseInfo.success(dropdownList);
    }

    @PostMapping("/copy-from-version")
    @ApiOperation("新增检验方案（从现有方案版本复制），返回方案与版本信息")
    public ResponseInfo<InspectionSchemeBasicSaveRespDTO> copySchemeFromVersion(@RequestBody @Validated InspectionSchemeCopyFromVersionReqVO reqVO) {
        InspectionSchemeCopyFromVersionDTO dto = BeanUtil.copyProperties(reqVO, InspectionSchemeCopyFromVersionDTO.class);
        return ResponseInfo.success(inspectionSchemeService.copySchemeFromVersion(dto));
    }

} 