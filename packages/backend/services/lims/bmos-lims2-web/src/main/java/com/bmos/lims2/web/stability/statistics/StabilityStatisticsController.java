package com.bmos.lims2.web.stability.statistics;

import cn.hutool.core.bean.BeanUtil;
import com.bmos.common.response.ResponseInfo;
import com.bmos.lims2.server.stability.statistics.dto.StabilityExperimentTypeOptionDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityPlanOptionDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsItemDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsQueryDTO;
import com.bmos.lims2.server.stability.statistics.service.StabilityStatisticsService;
import com.bmos.lims2.web.stability.statistics.vo.request.StabilityStatisticsQueryReqVO;
import com.bmos.lims2.web.stability.statistics.vo.response.StabilityStatisticsItemRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性统计查询Controller
 */
@RestController
@RequestMapping("/stability-statistics")
@Api(tags = "稳定性统计查询-接口")
@Validated
public class StabilityStatisticsController {

    @Autowired
    private StabilityStatisticsService stabilityStatisticsService;

    @PostMapping("/query")
    @ApiOperation("查询稳定性统计检项信息（表头+数据分离）")
    public ResponseInfo<com.bmos.lims2.web.stability.statistics.vo.response.StabilityStatisticsResultRespVO> query(
            @RequestBody @Validated StabilityStatisticsQueryReqVO reqVO) {
        StabilityStatisticsQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityStatisticsQueryDTO.class);
        com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsResultDTO result =
            stabilityStatisticsService.queryStatisticsResult(queryDTO);
        return ResponseInfo.success(BeanUtil.copyProperties(result,
            com.bmos.lims2.web.stability.statistics.vo.response.StabilityStatisticsResultRespVO.class));
    }

    @GetMapping("/plan-options")
    @ApiOperation("获取指定检品下的稳定性考察计划下拉列表")
    public ResponseInfo<List<StabilityPlanOptionDTO>> planOptions(
            @ApiParam(value = "检品ID", required = true) @RequestParam @NotNull Long materialId) {
        return ResponseInfo.success(stabilityStatisticsService.getPlanOptions(materialId));
    }

    @GetMapping("/experiment-type-options")
    @ApiOperation("获取指定计划下的试验类型下拉列表")
    public ResponseInfo<List<StabilityExperimentTypeOptionDTO>> experimentTypeOptions(
            @ApiParam(value = "计划ID", required = true) @RequestParam @NotNull Long planId) {
        return ResponseInfo.success(stabilityStatisticsService.getExperimentTypeOptions(planId));
    }

    @PostMapping("/export")
    @ApiOperation("导出稳定性统计检项数据")
    public void export(@RequestBody @Validated StabilityStatisticsQueryReqVO reqVO,
                       HttpServletResponse response) {
        StabilityStatisticsQueryDTO queryDTO = BeanUtil.copyProperties(reqVO, StabilityStatisticsQueryDTO.class);
        stabilityStatisticsService.exportStatisticsItems(queryDTO, response);
    }
}
