package com.bmos.lims2.web.stability.plan;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleReceiveDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityOverallSampleTakeDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityPlanSampleAddItemDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityTimepointBatchTakeDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityTimepointSampleQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOrderSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOverallSampleDetailDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityOverallSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilitySchemeExperimentTypeDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityTimepointSampleDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityTimepointSourceSampleDTO;
import com.bmos.lims2.server.stability.plan.service.StabilityInspectPlanService;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityOverallSampleQueryReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityOverallSampleReceiveReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityOverallSampleTakeReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityOverallAddSamplesReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityPlanSampleTakeReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityTimepointBatchTakeReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityTimepointSampleQueryReqVO;
import com.bmos.lims2.web.stability.plan.vo.response.*;
import com.bmos.lims2.server.inspect.order.service.InspectionOrderService;

import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性考察样品Controller
 *
 * 包含两类接口：
 *  1. 整体样品（lm_stability_plan_sample）的分页/详情/取样/接收
 *  2. 时间点检验单样品的取样/接收
 */
@RestController
@RequestMapping("/stability-plan-sample")
@Api(tags = "稳定性考察样品-接口")
@Validated
public class StabilityPlanSampleController {

    @Autowired
    private StabilityInspectPlanService stabilityInspectPlanService;

    @Autowired
    private InspectionOrderService inspectionOrderService;

    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

    // ══════════════════════ 整体样品 ══════════════════════

    @GetMapping("/overall/count")
    @ApiOperation("查询取样页签数量（常规检验取样 + 稳定性考察取样）")
    public ResponseInfo<SamplingTabCountRespVO> countSamplingTabs() {
        long regularCount = inspectionOrderService.countConfirmedOrdersForSampling();
        long stabilityCount = stabilityInspectPlanService.countPendingOverallSamples();
        return ResponseInfo.success(new SamplingTabCountRespVO(regularCount, stabilityCount));
    }

    @GetMapping("/overall/page")
    @ApiOperation("整体样品分页列表")
    public ResponseInfo<CommonPage<StabilityOverallSampleRespVO>> pageOverallSamples(
            @Validated StabilityOverallSampleQueryReqVO reqVO) {
        StabilityOverallSampleQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityOverallSampleQueryDTO.class);
        CommonPage<StabilityOverallSampleDTO> page = stabilityInspectPlanService.pageOverallSamples(queryDTO);

        CommonPage<StabilityOverallSampleRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), StabilityOverallSampleRespVO.class));

        return ResponseInfo.success(respPage);
    }

    @GetMapping("/overall/receive/page")
    @ApiOperation("稳定性样品接收列表（仅展示已取样待接收的批次）")
    public ResponseInfo<CommonPage<StabilityOverallSampleRespVO>> pageOverallSamplesForReceive(
            @Validated StabilityOverallSampleQueryReqVO reqVO) {
        StabilityOverallSampleQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityOverallSampleQueryDTO.class);

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

        if (reqVO.getMaterialId() == null && reqVO.getCategoryId() != null
                && (materialIds == null || materialIds.isEmpty())) {
            CommonPage<StabilityOverallSampleRespVO> empty = new CommonPage<>();
            empty.setPageNum(reqVO.getPageNum());
            empty.setPageSize(reqVO.getPageSize());
            empty.setTotal(0);
            empty.setList(java.util.Collections.emptyList());
            return ResponseInfo.success(empty);
        }

        if (cn.hutool.core.collection.CollUtil.isNotEmpty(materialIds)) {
            queryDTO.setMaterialIds(materialIds);
        }

        CommonPage<StabilityOverallSampleDTO> page = stabilityInspectPlanService.pageOverallSamplesForReceive(queryDTO);

        CommonPage<StabilityOverallSampleRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), StabilityOverallSampleRespVO.class));

        return ResponseInfo.success(respPage);
    }

    @GetMapping("/overall/{id}/detail")
    @ApiOperation("整体样品详情（按批次维度，含所有试验类型样品列表）")
    public ResponseInfo<StabilityOverallSampleDetailRespVO> getOverallSampleDetail(@PathVariable Long id) {
        StabilityOverallSampleDetailDTO dto = stabilityInspectPlanService.getOverallSampleDetail(id);
        StabilityOverallSampleDetailRespVO vo = BeanUtil.copyProperties(dto, StabilityOverallSampleDetailRespVO.class);
        if (dto.getSamples() != null) {
            vo.setSamples(BeanUtil.copyToList(dto.getSamples(),
                    StabilityOverallSampleDetailRespVO.SampleItemRespVO.class));
        }
        return ResponseInfo.success(vo);
    }

    @GetMapping("/overall/{id}/receive-detail")
    @ApiOperation("接收详情（按批次维度，仅含已取样待接收和已接收的样品）")
    public ResponseInfo<StabilityOverallSampleDetailRespVO> getOverallSampleDetailForReceive(@PathVariable Long id) {
        StabilityOverallSampleDetailDTO dto = stabilityInspectPlanService.getOverallSampleDetailForReceive(id);
        StabilityOverallSampleDetailRespVO vo = BeanUtil.copyProperties(dto, StabilityOverallSampleDetailRespVO.class);
        if (dto.getSamples() != null) {
            vo.setSamples(BeanUtil.copyToList(dto.getSamples(),
                    StabilityOverallSampleDetailRespVO.SampleItemRespVO.class));
        }
        return ResponseInfo.success(vo);
    }

    @PutMapping("/overall/{batchId}/take")
    @ApiOperation("整体批量取样（按批次，每个试验类型各创建一个lm_sample）")
    public ResponseInfo<Void> takeOverallSamples(@PathVariable Long batchId,
            @RequestBody @Validated StabilityOverallSampleTakeReqVO reqVO) {
        StabilityOverallSampleTakeDTO dto = new StabilityOverallSampleTakeDTO();
        dto.setBatchId(batchId);
        dto.setSamplerId(reqVO.getSamplerId());
        dto.setSamplerName(reqVO.getSamplerName());
        dto.setItems(BeanUtil.copyToList(reqVO.getItems(), StabilityOverallSampleTakeDTO.ItemDTO.class));
        stabilityInspectPlanService.takeOverallSamples(batchId, dto);
        return ResponseInfo.success();
    }

    @PutMapping("/overall/receive")
    @ApiOperation("整体批量接收（支持跨批次，每个试验类型样品状态→已接收，触发时间点任务生成）")
    public ResponseInfo<Void> receiveOverallSamples(
            @RequestBody @Validated List<StabilityOverallSampleReceiveReqVO> items) {
        stabilityInspectPlanService.receiveOverallSamples(
                BeanUtil.copyToList(items, StabilityOverallSampleReceiveDTO.class));
        return ResponseInfo.success();
    }

    @PostMapping("/overall/{batchId}/add-samples")
    @ApiOperation("整体取样-新增样品（手动追加试验类型样品，不来源于方案）")
    public ResponseInfo<List<StabilityOverallSampleDetailRespVO.SampleItemRespVO>> addOverallSamples(@PathVariable Long batchId,
            @RequestBody @Validated StabilityOverallAddSamplesReqVO reqVO) {
        List<StabilityPlanSampleAddItemDTO> items =
                BeanUtil.copyToList(reqVO.getItems(), StabilityPlanSampleAddItemDTO.class);
        List<StabilityOverallSampleDetailDTO.SampleItemDTO> result =
                stabilityInspectPlanService.addOverallSamples(batchId, items);
        return ResponseInfo.success(BeanUtil.copyToList(result, StabilityOverallSampleDetailRespVO.SampleItemRespVO.class));
    }

    @DeleteMapping("/overall/sample/{sampleId}")
    @ApiOperation("整体取样-删除手动新增样品（仅 manualAdded=true 且待取样状态可删除）")
    public ResponseInfo<Void> deleteManualOverallSample(@PathVariable Long sampleId) {
        stabilityInspectPlanService.deleteManualOverallSample(sampleId);
        return ResponseInfo.success();
    }

    @GetMapping("/plan/{planId}/scheme-experiment-types")
    @ApiOperation("查询计划关联方案的试验类型列表（用于新增样品下拉选项）")
    public ResponseInfo<List<StabilitySchemeExperimentTypeRespVO>> listSchemeExperimentTypes(
            @PathVariable Long planId) {
        List<StabilitySchemeExperimentTypeDTO> dtoList =
                stabilityInspectPlanService.listSchemeExperimentTypes(planId);
        return ResponseInfo.success(BeanUtil.copyToList(dtoList, StabilitySchemeExperimentTypeRespVO.class));
    }

    @GetMapping("/overall/scan-by-sample-no")
    @ApiOperation("扫码识别：根据样品编号查询批次整体样品详情（用于扫码批量接收）")
    public ResponseInfo<StabilityOverallSampleDetailRespVO> scanBySampleNo(
            @NotNull(message = "样品编号不能为空") @RequestParam String sampleNo) {
        StabilityOverallSampleDetailDTO dto = stabilityInspectPlanService.getOverallSampleDetailBySampleNo(sampleNo);
        if (dto == null) {
            return ResponseInfo.success(null);
        }
        StabilityOverallSampleDetailRespVO vo = BeanUtil.copyProperties(dto, StabilityOverallSampleDetailRespVO.class);
        if (dto.getSamples() != null) {
            vo.setSamples(BeanUtil.copyToList(dto.getSamples(),
                    StabilityOverallSampleDetailRespVO.SampleItemRespVO.class));
        }
        return ResponseInfo.success(vo);
    }

    // ══════════════════════ 时间点检验单样品 ══════════════════════

    @GetMapping("/order-samples")
    @ApiOperation("查询时间点任务的样品列表（含稳定性上下文）")
    public ResponseInfo<List<StabilityPlanSampleRespVO>> listOrderSamples(
            @NotNull(message = "时间点任务ID不能为空") @RequestParam Long timepointTaskId) {
        List<StabilityOrderSampleDTO> dtoList = stabilityInspectPlanService.listOrderSamples(timepointTaskId);
        return ResponseInfo.success(BeanUtil.copyToList(dtoList, StabilityPlanSampleRespVO.class));
    }

    @PutMapping("/{timepointTaskId}/take")
    @ApiOperation("时间点样品取样（取样即接收，完成后自动进入检测）")
    public ResponseInfo<Void> takeSample(@PathVariable Long timepointTaskId,
            @RequestBody @Validated StabilityPlanSampleTakeReqVO reqVO) {
        stabilityInspectPlanService.takeStabilitySample(timepointTaskId,
                reqVO.getSamplerName(), reqVO.getSamplerId());
        return ResponseInfo.success();
    }

    // ══════════════════════ 时间点取样（新接口） ══════════════════════

    @GetMapping("/timepoint/page")
    @ApiOperation("时间点取样分页列表（待取样，跨所有计划）")
    public ResponseInfo<CommonPage<StabilityTimepointSampleRespVO>> pageTimepointSamples(
            @Validated StabilityTimepointSampleQueryReqVO reqVO) {
        StabilityTimepointSampleQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityTimepointSampleQueryDTO.class);
        CommonPage<StabilityTimepointSampleDTO> page = stabilityInspectPlanService.pageTimepointSamples(queryDTO);
        CommonPage<StabilityTimepointSampleRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), StabilityTimepointSampleRespVO.class));
        return ResponseInfo.success(respPage);
    }

    @GetMapping("/timepoint/{timepointTaskId}/source-samples")
    @ApiOperation("查询时间点取样对象列表（取样对象下拉选项，同批次同试验类型已接收整体样品）")
    public ResponseInfo<List<StabilityTimepointSourceSampleRespVO>> listSourceSamples(
            @PathVariable Long timepointTaskId) {
        List<StabilityTimepointSourceSampleDTO> dtoList =
                stabilityInspectPlanService.listSourceSamples(timepointTaskId);
        return ResponseInfo.success(BeanUtil.copyToList(dtoList, StabilityTimepointSourceSampleRespVO.class));
    }

    @PutMapping("/timepoint/batch-take")
    @ApiOperation("时间点批量取样提交（多行同时提交，取样即接收，状态流转至进行中）")
    public ResponseInfo<Void> batchTakeTimepointSamples(
            @RequestBody @Validated StabilityTimepointBatchTakeReqVO reqVO) {
        StabilityTimepointBatchTakeDTO dto = new StabilityTimepointBatchTakeDTO();
        dto.setItems(BeanUtil.copyToList(reqVO.getItems(), StabilityTimepointBatchTakeDTO.ItemDTO.class));
        stabilityInspectPlanService.batchTakeTimepointSamples(dto);
        return ResponseInfo.success();
    }
}

