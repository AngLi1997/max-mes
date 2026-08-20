package com.bmos.lims2.web.stability.plan;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanPauseDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanQueryDTO;
import com.bmos.lims2.server.stability.plan.dto.request.StabilityInspectPlanSaveDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityInspectPlanDetailDTO;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityInspectPlanDTO;
import com.bmos.lims2.server.stability.plan.service.StabilityInspectPlanService;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityInspectPlanPauseReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityInspectPlanQueryReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityInspectPlanResumeReqVO;
import com.bmos.lims2.web.stability.plan.vo.request.StabilityInspectPlanSaveReqVO;
import com.bmos.lims2.web.stability.plan.vo.response.StabilityInspectPlanDetailRespVO;
import com.bmos.lims2.web.stability.plan.vo.response.StabilityInspectPlanRespVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 稳定性考察计划Controller
 */
@RestController
@RequestMapping("/stability-inspect-plan")
@Api(tags = "稳定性考察计划-接口")
@Validated
public class StabilityInspectPlanController {

    @Autowired
    private StabilityInspectPlanService stabilityInspectPlanService;

    @Autowired
    private com.bmos.lims2.server.material.service.MaterialService materialService;

    @GetMapping("/page")
    @ApiOperation("分页查询稳定性考察计划列表")
    public ResponseInfo<CommonPage<StabilityInspectPlanRespVO>> pageStabilityInspectPlan(
            @Validated StabilityInspectPlanQueryReqVO reqVO) {
        StabilityInspectPlanQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityInspectPlanQueryDTO.class);

        if (reqVO.getMaterialId() != null) {
            queryDTO.setMaterialIds(java.util.Collections.singletonList(reqVO.getMaterialId()));
        } else if (reqVO.getMaterialCategoryId() != null) {
            java.util.List<com.bmos.lims2.server.material.dto.MaterialDTO> materials =
                    materialService.getAllByCategoryId(reqVO.getMaterialCategoryId());
            if (cn.hutool.core.collection.CollUtil.isNotEmpty(materials)) {
                java.util.List<Long> ids = materials.stream()
                        .filter(m -> java.lang.Boolean.TRUE.equals(m.getStatus()))
                        .map(com.bmos.mybatis.dataobject.BaseDO::getId)
                        .collect(java.util.stream.Collectors.toList());
                if (ids.isEmpty()) {
                    CommonPage<StabilityInspectPlanRespVO> empty = new CommonPage<>();
                    empty.setPageNum(reqVO.getPageNum());
                    empty.setPageSize(reqVO.getPageSize());
                    empty.setTotal(0);
                    empty.setList(java.util.Collections.emptyList());
                    return ResponseInfo.success(empty);
                }
                queryDTO.setMaterialIds(ids);
            } else {
                CommonPage<StabilityInspectPlanRespVO> empty = new CommonPage<>();
                empty.setPageNum(reqVO.getPageNum());
                empty.setPageSize(reqVO.getPageSize());
                empty.setTotal(0);
                empty.setList(java.util.Collections.emptyList());
                return ResponseInfo.success(empty);
            }
        }

        CommonPage<StabilityInspectPlanDTO> page = stabilityInspectPlanService.pageStabilityInspectPlan(queryDTO);

        CommonPage<StabilityInspectPlanRespVO> respPage = new CommonPage<>();
        respPage.setPageNum(page.getPageNum());
        respPage.setPageSize(page.getPageSize());
        respPage.setTotal(page.getTotal());
        respPage.setList(BeanUtil.copyToList(page.getList(), StabilityInspectPlanRespVO.class));

        return ResponseInfo.success(respPage);
    }

    @GetMapping("/{id}")
    @ApiOperation("查询稳定性考察计划详情")
    public ResponseInfo<StabilityInspectPlanDetailRespVO> getStabilityInspectPlan(@PathVariable Long id) {
        StabilityInspectPlanDetailDTO dto = stabilityInspectPlanService.getStabilityInspectPlan(id);
        StabilityInspectPlanDetailRespVO vo = BeanUtil.copyProperties(dto, StabilityInspectPlanDetailRespVO.class);
        if (dto.getBatches() != null) {
            vo.setBatches(BeanUtil.copyToList(dto.getBatches(), StabilityInspectPlanDetailRespVO.BatchVO.class));
        }
        vo.setMatrix(dto.getMatrix());
        return ResponseInfo.success(vo);
    }

    @PostMapping("/save")
    @ApiOperation("新增稳定性考察计划")
    public ResponseInfo<Long> saveStabilityInspectPlan(
            @RequestBody @Validated StabilityInspectPlanSaveReqVO reqVO) {
        StabilityInspectPlanSaveDTO saveDTO = buildSaveDTO(reqVO);
        Long id = stabilityInspectPlanService.saveStabilityInspectPlan(saveDTO);
        return ResponseInfo.success(id);
    }

    @PutMapping("/update")
    @ApiOperation("修改稳定性考察计划（仅待开始状态可修改）")
    public ResponseInfo<Void> updateStabilityInspectPlan(
            @RequestBody @Validated StabilityInspectPlanSaveReqVO reqVO) {
        StabilityInspectPlanSaveDTO saveDTO = buildSaveDTO(reqVO);
        stabilityInspectPlanService.updateStabilityInspectPlan(saveDTO);
        return ResponseInfo.success();
    }

    @PutMapping("/pause")
    @ApiOperation("暂停稳定性考察计划")
    public ResponseInfo<Void> pauseStabilityInspectPlan(
            @RequestBody @Validated StabilityInspectPlanPauseReqVO reqVO) {
        StabilityInspectPlanPauseDTO pauseDTO = BeanUtil.copyProperties(reqVO, StabilityInspectPlanPauseDTO.class);
        stabilityInspectPlanService.pauseStabilityInspectPlan(pauseDTO);
        return ResponseInfo.success();
    }

    @PutMapping("/{id}/resume")
    @ApiOperation("恢复稳定性考察计划")
    public ResponseInfo<Void> resumeStabilityInspectPlan(@PathVariable Long id,
            @RequestBody(required = false) StabilityInspectPlanResumeReqVO reqVO) {
        Long schemeVersionId = reqVO != null ? reqVO.getSchemeVersionId() : null;
        stabilityInspectPlanService.resumeStabilityInspectPlan(id, schemeVersionId);
        return ResponseInfo.success();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("撤销稳定性考察计划（仅待开始状态可操作）")
    public ResponseInfo<Void> withdrawStabilityInspectPlan(@PathVariable Long id) {
        stabilityInspectPlanService.withdrawPlan(id);
        return ResponseInfo.success();
    }

    @PutMapping("/batch/{batchId}/zero-month-order")
    @ApiOperation("选择0月检验单（同批号存在多条常规检验单时由用户手动指定）")
    public ResponseInfo<Void> selectZeroMonthOrder(
            @PathVariable Long batchId,
            @RequestParam Long inspectionOrderId) {
        stabilityInspectPlanService.selectZeroMonthOrder(batchId, inspectionOrderId);
        return ResponseInfo.success();
    }

    @PostMapping("/dev/trigger-due-timepoint-tasks")
    @ApiOperation("【测试】手动触发到期时间点任务（仅供开发调试）")
    public ResponseInfo<Integer> devTriggerDueTimepointTasks() {
        int count = stabilityInspectPlanService.triggerDueTimepointTasks();
        return ResponseInfo.success(count);
    }

    private StabilityInspectPlanSaveDTO buildSaveDTO(StabilityInspectPlanSaveReqVO reqVO) {
        StabilityInspectPlanSaveDTO saveDTO = new StabilityInspectPlanSaveDTO();
        saveDTO.setId(reqVO.getId());
        saveDTO.setMaterialId(reqVO.getMaterialId());
        saveDTO.setMaterialName(reqVO.getMaterialName());
        saveDTO.setMaterialCode(reqVO.getMaterialCode());
        saveDTO.setMaterialSpec(reqVO.getMaterialSpec());
        saveDTO.setSchemeId(reqVO.getSchemeId());
        saveDTO.setSchemeVersionId(reqVO.getSchemeVersionId());
        saveDTO.setRemark(reqVO.getRemark());

        if (reqVO.getBatches() != null) {
            List<StabilityInspectPlanSaveDTO.BatchDTO> batchDTOs = new ArrayList<>();
            for (StabilityInspectPlanSaveReqVO.BatchVO batchVO : reqVO.getBatches()) {
                StabilityInspectPlanSaveDTO.BatchDTO batchDTO = new StabilityInspectPlanSaveDTO.BatchDTO();
                batchDTO.setBatchNo(batchVO.getBatchNo());
                batchDTO.setProductionDate(batchVO.getProductionDate());
                batchDTOs.add(batchDTO);
            }
            saveDTO.setBatches(batchDTOs);
        }

        return saveDTO;
    }
}
