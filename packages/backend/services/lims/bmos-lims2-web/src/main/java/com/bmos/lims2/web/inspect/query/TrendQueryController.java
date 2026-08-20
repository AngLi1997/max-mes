package com.bmos.lims2.web.inspect.query;

import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.inspect.query.dto.TrendQueryDTO;
import com.bmos.lims2.server.inspect.query.dto.TrendValueSeriesDTO;
import com.bmos.lims2.server.inspect.query.service.TrendOptionService;
import com.bmos.lims2.server.inspect.query.service.TrendQueryService;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO;
import com.bmos.lims2.server.inspect.scheme.dto.response.SchemeVersionOptionDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionScheme;
import com.bmos.lims2.server.task.dto.SchemeParameterDTO;
import com.bmos.lims2.web.inspect.query.vo.request.TrendQueryReqVO;
import com.bmos.lims2.web.inspect.query.vo.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 趋势查询接口
 * @Author: yigaohui
 * @Date: 2025/09/05 12:15
 */
@RestController
@RequestMapping("/inspect/trend")
@Api(tags = "趋势查询")
@Validated
public class TrendQueryController {

    private final TrendOptionService optionService;
    private final TrendQueryService trendQueryService;

    public TrendQueryController(TrendOptionService optionService, TrendQueryService trendQueryService) {
        this.optionService = optionService;
        this.trendQueryService = trendQueryService;
    }

    @GetMapping("/options/schemes")
    @ApiOperation("联动：按检品获取检验方案列表")
    public ResponseInfo<List<SchemeOptionRespVO>> listSchemes(@RequestParam @NotNull Long materialId) {
        List<InspectionScheme> schemes = optionService.listSchemesByMaterial(materialId);
        List<SchemeOptionRespVO> resp = schemes.stream().map(s -> {
            SchemeOptionRespVO vo = new SchemeOptionRespVO();
            vo.setId(s.getId());
            vo.setName(s.getName());
            return vo;
        }).collect(Collectors.toList());
        return ResponseInfo.success(resp);
    }

    @GetMapping("/options/versions")
    @ApiOperation("联动：按方案获取版本列表")
    public ResponseInfo<List<SchemeVersionOptionRespVO>> listVersions(@RequestParam @NotNull Long schemeId) {
        List<SchemeVersionOptionDTO> versions = optionService.listVersionsByScheme(schemeId);
        List<SchemeVersionOptionRespVO> resp = versions.stream().map(v -> {
            SchemeVersionOptionRespVO vo = new SchemeVersionOptionRespVO();
            vo.setId(v.getId());
            vo.setVersionNo(v.getVersionNo());
            vo.setStatus(v.getStatus());
            return vo;
        }).collect(Collectors.toList());
        return ResponseInfo.success(resp);
    }

    @GetMapping("/options/parameters")
    @ApiOperation("联动：按方案获取检验项目/分析项列表（使用生效版本）")
    public ResponseInfo<List<SchemeParameterOptionRespVO>> listParameters(@RequestParam @NotNull Long schemeId) {
        List<SchemeParameterDTO> list = optionService.listParametersByScheme(schemeId);
        List<SchemeParameterOptionRespVO> resp = list.stream().map(p -> {
            SchemeParameterOptionRespVO vo = new SchemeParameterOptionRespVO();
            vo.setSchemeItemId(p.getSchemeItemId());
            vo.setInspectItemId(p.getInspectItemId());
            vo.setInspectItemName(p.getInspectItemName());
            vo.setInspectItemCode(p.getInspectItemCode());
            vo.setSchemeParameterId(p.getSchemeParameterId());
            vo.setParameterId(p.getParameterId());
            vo.setParameterName(p.getParameterName());
            vo.setParameterCode(p.getParameterCode());
            return vo;
        }).collect(Collectors.toList());
        return ResponseInfo.success(resp);
    }

    @GetMapping("/options/datapoints")
    @ApiOperation("联动：按分析项配置获取数值型数据点列表")
    public ResponseInfo<List<DataPointOptionRespVO>> listNumericPoints(@RequestParam @NotNull Long parameterConfigId) {
        List<InspectionSchemeDataPointDTO> list = optionService.listNumericDataPointsByParameterConfig(parameterConfigId);
        List<DataPointOptionRespVO> resp = list.stream().map(dp -> {
            DataPointOptionRespVO vo = new DataPointOptionRespVO();
            vo.setId(dp.getId());
            vo.setDataPointId(dp.getDataPointId());
            vo.setName(dp.getName());
            return vo;
        }).collect(Collectors.toList());
        return ResponseInfo.success(resp);
    }

    @PostMapping("/query")
    @ApiOperation("查询数据点数值趋势")
    public ResponseInfo<TrendValueSeriesRespVO> queryTrend(@RequestBody @Valid TrendQueryReqVO reqVO) {
        TrendQueryDTO dto = new TrendQueryDTO();
        dto.setMaterialId(reqVO.getMaterialId());
        dto.setSchemeId(reqVO.getSchemeId());
        dto.setInspectItemId(reqVO.getInspectItemId());
        dto.setParameterId(reqVO.getParameterId());
        dto.setDataPointName(reqVO.getDataPointName());
        dto.setRequestStartTime(reqVO.getRequestStartTime());
        dto.setRequestEndTime(reqVO.getRequestEndTime());

        TrendValueSeriesDTO series = trendQueryService.queryNumericTrend(dto);
        TrendValueSeriesRespVO respVO = new TrendValueSeriesRespVO();
        respVO.setXAxisBatchNos(series.getXAxisBatchNos());
        respVO.setTrendLines(series.getTrendLines());
        List<TrendValueSeriesRespVO.Point> points = series.getPoints().stream().map(p -> {
            TrendValueSeriesRespVO.Point vo = new TrendValueSeriesRespVO.Point();
            vo.setInspectionOrderId(p.getInspectionOrderId());
            vo.setInspectionOrderNo(p.getInspectionOrderNo());
            vo.setBatchNo(p.getBatchNo());
            vo.setRequestTime(p.getRequestTime());
            vo.setValueNumber(p.getValueNumber());
            return vo;
        }).collect(Collectors.toList());
        respVO.setPoints(points);
        return ResponseInfo.success(respVO);
    }
}


