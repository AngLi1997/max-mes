package com.bmos.lims2.server.inspect.query.service.impl;

import com.bmos.lims2.server.inspect.entry.dto.TrendValuePointDTO;
import com.bmos.lims2.server.inspect.entry.mapper.InspectionEntryRecordMapper;
import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointTrendDTO;
import com.bmos.lims2.server.inspect.parameter.service.InspectParameterDataPointTrendService;
import com.bmos.lims2.server.inspect.query.dto.TrendQueryDTO;
import com.bmos.lims2.server.inspect.query.dto.TrendValueSeriesDTO;
import com.bmos.lims2.server.inspect.query.service.TrendQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 趋势查询服务实现
 * @Author: yigaohui
 * @Date: 2025/09/05 11:42
 */
@Service
public class TrendQueryServiceImpl implements TrendQueryService {

    private final InspectionEntryRecordMapper entryRecordMapper;
    private final InspectParameterDataPointTrendService trendService;

    public TrendQueryServiceImpl(InspectionEntryRecordMapper entryRecordMapper,
                                 InspectParameterDataPointTrendService trendService) {
        this.entryRecordMapper = entryRecordMapper;
        this.trendService = trendService;
    }

    @Override
    @Transactional(readOnly = true)
    public TrendValueSeriesDTO queryNumericTrend(TrendQueryDTO queryDTO) {
        List<TrendValuePointDTO> points = entryRecordMapper.selectNumericTrendValues(
                queryDTO.getMaterialId(),
                queryDTO.getSchemeId(),
                queryDTO.getInspectItemId(),
                queryDTO.getParameterId(),
                queryDTO.getDataPointName(),
                queryDTO.getRequestStartTime(),
                queryDTO.getRequestEndTime());

        points = points.stream()
                .sorted(Comparator.comparing(TrendValuePointDTO::getRequestTime)
                        .thenComparing(TrendValuePointDTO::getInspectionOrderId))
                .collect(java.util.stream.Collectors.toList());

        TrendValueSeriesDTO resp = new TrendValueSeriesDTO();
        resp.setPoints(points);
        resp.setXAxisBatchNos(points.stream().map(TrendValuePointDTO::getBatchNo).collect(Collectors.toList()));
// TODO: 2025/10/20 趋势线查询有点疑问，这里存在多个方案多个数据点
//		// 填充趋势线配置：按参数ID+数据点名称查询
//		List<InspectParameterDataPointTrendDTO> trendLines = trendService.getTrendsByDataPointName(
//				queryDTO.getParameterId(), queryDTO.getDataPointName());
//		resp.setTrendLines(trendLines == null ? java.util.Collections.emptyList() : trendLines);
		return resp;
    }
}


