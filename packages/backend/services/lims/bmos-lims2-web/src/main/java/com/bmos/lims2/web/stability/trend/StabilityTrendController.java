package com.bmos.lims2.web.stability.trend;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.stability.statistics.dto.StabilitySchemeOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendBatchOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendChartResultDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendDataPointOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendExperimentTypeOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendParameterOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendQueryDTO;
import com.bmos.lims2.server.stability.trend.dto.StabilityTrendTableResultDTO;
import com.bmos.lims2.server.stability.trend.service.StabilityTrendService;
import com.bmos.lims2.web.stability.trend.vo.request.StabilityTrendQueryReqVO;
import com.bmos.lims2.web.stability.trend.vo.response.StabilityTrendChartResultRespVO;
import com.bmos.lims2.web.stability.trend.vo.response.StabilityTrendTableResultRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性趋势查询Controller
 */
@RestController
@RequestMapping("/stability-trend")
@Api(tags = "稳定性趋势查询-接口")
@Validated
public class StabilityTrendController {

    @Autowired
    private StabilityTrendService stabilityTrendService;

    @GetMapping("/scheme-options")
    @ApiOperation("获取指定检品下的稳定性方案下拉列表")
    public ResponseInfo<List<StabilitySchemeOptionDTO>> schemeOptions(
            @ApiParam(value = "检品ID", required = true) @RequestParam @NotNull Long materialId) {
        return ResponseInfo.success(stabilityTrendService.getSchemeOptions(materialId));
    }

    @GetMapping("/experiment-type-options")
    @ApiOperation("获取指定方案版本下的试验类型下拉列表")
    public ResponseInfo<List<StabilityTrendExperimentTypeOptionDTO>> experimentTypeOptions(
            @ApiParam(value = "方案版本ID", required = true) @RequestParam @NotNull Long versionId) {
        return ResponseInfo.success(stabilityTrendService.getExperimentTypeOptions(versionId));
    }

    @GetMapping("/parameter-options")
    @ApiOperation("获取指定方案版本下的分析项下拉列表")
    public ResponseInfo<List<StabilityTrendParameterOptionDTO>> parameterOptions(
            @ApiParam(value = "方案版本ID", required = true) @RequestParam @NotNull Long versionId) {
        return ResponseInfo.success(stabilityTrendService.getParameterOptions(versionId));
    }

    @GetMapping("/data-point-options")
    @ApiOperation("获取指定方案版本+分析项下的数据点下拉列表")
    public ResponseInfo<List<StabilityTrendDataPointOptionDTO>> dataPointOptions(
            @ApiParam(value = "方案版本ID", required = true) @RequestParam @NotNull Long versionId,
            @ApiParam(value = "分析项ID", required = true) @RequestParam @NotNull Long parameterId) {
        return ResponseInfo.success(stabilityTrendService.getDataPointOptions(versionId, parameterId));
    }

    @GetMapping("/batch-options")
    @ApiOperation("获取指定方案版本+试验类型下的批号下拉列表")
    public ResponseInfo<List<StabilityTrendBatchOptionDTO>> batchOptions(
            @ApiParam(value = "方案版本ID", required = true) @RequestParam @NotNull Long versionId,
            @ApiParam(value = "试验类型", required = true) @RequestParam @NotBlank String experimentType,
            @ApiParam(value = "储存条件", required = true) @RequestParam @NotBlank String storageCondition) {
        return ResponseInfo.success(stabilityTrendService.getBatchOptions(versionId, experimentType, storageCondition));
    }

    @PostMapping("/query")
    @ApiOperation("查询稳定性趋势数据（表格：基础信息+时间点列+数据行）")
    public ResponseInfo<StabilityTrendTableResultRespVO> query(
            @RequestBody @Validated StabilityTrendQueryReqVO reqVO) {
        StabilityTrendQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityTrendQueryDTO.class);
        StabilityTrendTableResultDTO result = stabilityTrendService.queryTrendTable(queryDTO);

        StabilityTrendTableResultRespVO respVO = new StabilityTrendTableResultRespVO();
        respVO.setBasicInfo(BeanUtil.copyProperties(result.getBasicInfo(), StabilityTrendTableResultRespVO.BasicInfo.class));

        List<StabilityTrendTableResultRespVO.TimepointColumn> timepointColumns = result.getTimepointColumns().stream()
                .map(col -> BeanUtil.copyProperties(col, StabilityTrendTableResultRespVO.TimepointColumn.class))
                .collect(java.util.stream.Collectors.toList());
        respVO.setTimepointColumns(timepointColumns);

        List<StabilityTrendTableResultRespVO.DataRow> dataRows = result.getDataRows().stream()
                .map(row -> {
                    StabilityTrendTableResultRespVO.DataRow dataRow = new StabilityTrendTableResultRespVO.DataRow();
                    dataRow.setBatchNo(row.getBatchNo());
                    List<StabilityTrendTableResultRespVO.TimepointValue> tvList = row.getTimepointValues().stream()
                            .map(tv -> BeanUtil.copyProperties(tv, StabilityTrendTableResultRespVO.TimepointValue.class))
                            .collect(java.util.stream.Collectors.toList());
                    dataRow.setTimepointValues(tvList);
                    return dataRow;
                })
                .collect(java.util.stream.Collectors.toList());
        respVO.setDataRows(dataRows);

        return ResponseInfo.success(respVO);
    }

    @PostMapping("/chart")
    @ApiOperation("查询稳定性趋势图表数据（ECharts结构）")
    public ResponseInfo<StabilityTrendChartResultRespVO> chart(
            @RequestBody @Validated StabilityTrendQueryReqVO reqVO) {
        StabilityTrendQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityTrendQueryDTO.class);
        StabilityTrendChartResultDTO result = stabilityTrendService.queryTrendChart(queryDTO);
        return ResponseInfo.success(BeanUtil.copyProperties(result, StabilityTrendChartResultRespVO.class));
    }

    @PostMapping("/export")
    @ApiOperation("导出稳定性趋势数据")
    public void export(@RequestBody @Validated StabilityTrendQueryReqVO reqVO,
                       HttpServletResponse response) {
        StabilityTrendQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityTrendQueryDTO.class);
        stabilityTrendService.exportTrendData(queryDTO, response);
    }
}
