package com.bmos.lims2.web.inspect.entry;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.entry.convert.InspectionEntryConvert;
import com.bmos.lims2.server.inspect.entry.dto.*;
import com.bmos.lims2.server.inspect.entry.service.InspectionEntryService;
import com.bmos.lims2.server.inspect.entry.vo.AnalysisItemEntryQueryVO;
import com.bmos.lims2.server.inspect.entry.vo.BatchEntryVO;
import com.bmos.lims2.server.inspect.entry.vo.BatchJudgmentVO;
import com.bmos.lims2.server.inspect.entry.vo.BatchTestTimeVO;
import com.bmos.lims2.server.inspect.entry.vo.InspectionOrderEntryQueryVO;
import com.bmos.mybatis.page.CommonPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 检验录入操作控制器
 * <p>
 * 统一的录入操作接口，包括批量录入数据点、批量设置检验时间等
 * 避免在分析项录入和检验单录入控制器中重复相同的接口
 *
 * @author system
 * @since 2025/01/30
 */
@Api(tags = "检验录入操作接口")
@RestController
@RequestMapping("/api/inspection-entry/")
public class InspectionEntryOperationController {

    @Autowired
    private InspectionEntryService inspectionEntryService;

    @ApiOperation("批量录入数据点")
    @PostMapping("/batch-save")
    public ResponseInfo<Integer> batchSaveEntryRecords(
            @ApiParam("批量录入数据") @RequestBody @Valid BatchEntryVO batchEntryVO) {

        BatchEntryDTO batchEntryDTO = InspectionEntryConvert.INSTANCE.toDTO(batchEntryVO);
        int savedCount = inspectionEntryService.batchSaveEntryRecords(batchEntryDTO);
        return ResponseInfo.success(savedCount);
    }

    @ApiOperation("批量设置检验时间")
    @PostMapping("/batch-test-time")
    public ResponseInfo<Integer> batchUpdateTestTime(
            @ApiParam("批量设置检验时间数据") @RequestBody @Valid List<BatchTestTimeVO> batchTestTimeVO) {

        List<BatchTestTimeDTO> batchTestTimeDTO = BeanUtil.copyToList(batchTestTimeVO, BatchTestTimeDTO.class);
        int updatedCount = inspectionEntryService.batchUpdateTestTime(batchTestTimeDTO);
        return ResponseInfo.success(updatedCount);
    }

    @ApiOperation("批量录入判定结论")
    @PostMapping("/batch-judgment")
    public ResponseInfo<Integer> batchUpdateJudgment(
            @ApiParam("批量录入判定结论数据") @RequestBody @Valid List<BatchJudgmentVO> list) {

        List<BatchJudgmentDTO> dtoList = BeanUtil.copyToList(list, BatchJudgmentDTO.class);
        int updatedCount = inspectionEntryService.batchUpdateJudgment(dtoList);
        return ResponseInfo.success(updatedCount);
    }

    @ApiOperation("根据数据点配置ID和任务ID查询录入记录")
    @GetMapping("/records/data-point-config/{dataPointConfigId}")
    public ResponseInfo<List<InspectionEntryRecordDTO>> getEntryRecordsBySchemeDataPointId(
            @ApiParam("数据点配置ID") @PathVariable Long dataPointConfigId,
            @ApiParam("任务ID") @RequestParam Long taskId) {

        List<InspectionEntryRecordDTO> records = inspectionEntryService.getEntryRecordsBySchemeDataPointIdAndTaskId(dataPointConfigId, taskId);
        return ResponseInfo.success(records);
    }

    @ApiOperation("根据任务ID查询录入记录")
    @GetMapping("/records/task/{taskId}")
    public ResponseInfo<List<InspectionEntryRecordDTO>> getEntryRecordsByTaskId(
            @ApiParam("任务ID") @PathVariable Long taskId) {

        List<InspectionEntryRecordDTO> records = inspectionEntryService.getEntryRecordsByTaskId(taskId);
        return ResponseInfo.success(records);
    }

    @ApiOperation("根据检验单ID和分析项ID查询录入记录")
    @GetMapping("/records")
    public ResponseInfo<List<InspectionEntryRecordDTO>> getEntryRecordsByInspectionOrderIdAndParameterId(
            @ApiParam("检验单ID") @RequestParam Long inspectionOrderId,
            @ApiParam("分析项ID") @RequestParam Long parameterId) {

        List<InspectionEntryRecordDTO> records = inspectionEntryService
                .getEntryRecordsByInspectionOrderIdAndParameterId(inspectionOrderId, parameterId);
        return ResponseInfo.success(records);
    }

    @ApiOperation("分析项录入列表查询")
    @PostMapping("/analysis-item/page")
    public ResponseInfo<CommonPage<AnalysisItemEntryDTO>> getAnalysisItemEntryPage(
            @ApiParam("查询条件") @RequestBody @Valid AnalysisItemEntryQueryVO queryVO) {

        AnalysisItemEntryQueryDTO queryDTO = inspectionEntryService.setAnalysisItemEntryPermission(queryVO);
        queryDTO.setOwnerOnly(true);
        CommonPage<AnalysisItemEntryDTO> page = inspectionEntryService.getAnalysisItemEntryPage(queryDTO);
        return ResponseInfo.success(page);
    }

    @ApiOperation("检验单录入列表查询")
    @PostMapping("/inspection-order/page")
    public ResponseInfo<CommonPage<InspectionOrderEntryDTO>> getInspectionOrderEntryPage(
            @ApiParam("查询条件") @RequestBody @Valid InspectionOrderEntryQueryVO queryVO) {

        InspectionOrderEntryQueryDTO queryDTO = inspectionEntryService.setInspectionOrderEntryPermission(queryVO);
        queryDTO.setOwnerOnly(true);
        CommonPage<InspectionOrderEntryDTO> page = inspectionEntryService.getInspectionOrderEntryPage(queryDTO);
        return ResponseInfo.success(page);
    }

    @ApiOperation("分析项录入统计（全部/未完成/异常）")
    @PostMapping("/analysis-item/stats")
    public ResponseInfo<EntryStatsDTO> analysisItemStats(
            @ApiParam("查询条件") @RequestBody @Valid AnalysisItemEntryQueryVO queryVO) {

        AnalysisItemEntryQueryDTO queryDTO = inspectionEntryService.setAnalysisItemEntryPermission(queryVO);
        queryDTO.setOwnerOnly(true);

        EntryStatsDTO stats = inspectionEntryService.getAnalysisItemStats(queryDTO);
        return ResponseInfo.success(stats);
    }

    @ApiOperation("检验单录入统计（全部/未完成/异常）")
    @PostMapping("/inspection-order/stats")
    public ResponseInfo<EntryStatsDTO> inspectionOrderStats(
            @ApiParam("查询条件") @RequestBody @Valid InspectionOrderEntryQueryVO queryVO) {

        InspectionOrderEntryQueryDTO queryDTO = inspectionEntryService.setInspectionOrderEntryPermission(queryVO);
        queryDTO.setOwnerOnly(true);
        EntryStatsDTO stats = inspectionEntryService.getInspectionOrderStats(queryDTO);
        return ResponseInfo.success(stats);
    }

    @ApiOperation("ELN同步检验录入（自动判断新增/修改）")
    @PostMapping("/eln-sync")
    public ResponseInfo<Integer> upsertFromEln(
            @ApiParam("ELN同步批量录入数据") @RequestBody @Valid BatchEntryVO batchEntryVO) {
        BatchEntryDTO batchEntryDTO = InspectionEntryConvert.INSTANCE.toDTO(batchEntryVO);
        int affected = inspectionEntryService.upsertEntryRecordsFromEln(batchEntryDTO);
        return ResponseInfo.success(affected);
    }
}
