package com.bmos.lims2.server.stability.trend.service.impl;

import com.alibaba.excel.EasyExcel;
import com.bmos.lims2.common.constants.DictCodeConstant;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.lims2.server.stability.plan.entity.StabilityInspectPlan;
import com.bmos.lims2.server.stability.plan.entity.StabilityInspectPlanBatch;
import com.bmos.lims2.server.stability.plan.mapper.StabilityInspectPlanBatchMapper;
import com.bmos.lims2.server.stability.plan.mapper.StabilityInspectPlanMapper;
import com.bmos.lims2.server.stability.statistics.dto.StabilitySchemeOptionDTO;
import com.bmos.lims2.server.stability.trend.dto.*;
import com.bmos.lims2.server.stability.trend.mapper.StabilityTrendMapper;
import com.bmos.lims2.server.stability.trend.service.StabilityTrendService;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import com.bmos.platform.facade.dict.feign.DictFeign;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 稳定性趋势查询ServiceImpl
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StabilityTrendServiceImpl implements StabilityTrendService {

    private final StabilityTrendMapper stabilityTrendMapper;
    private final DictFeign dictFeign;
    private final StabilityInspectPlanMapper planMapper;
    private final StabilityInspectPlanBatchMapper batchMapper;
    private final InspectionOrderMapper inspectionOrderMapper;

    @Override
    public List<StabilitySchemeOptionDTO> getSchemeOptions(Long materialId) {
        return stabilityTrendMapper.selectSchemeOptions(materialId);
    }

    @Override
    public List<StabilityTrendExperimentTypeOptionDTO> getExperimentTypeOptions(Long versionId) {
        List<StabilityTrendExperimentTypeOptionDTO> options = stabilityTrendMapper.selectExperimentTypeOptions(versionId);
        if (options == null || options.isEmpty()) {
            return options;
        }
        // 查字典，批量填充试验类型名称
        java.util.Map<String, String> codeLabelMap = new java.util.HashMap<>();
        try {
            DictDetailFeignVO dictDetail = FeignUtils.handleRequest(
                    data -> dictFeign.selectDictDetailByCode(data), DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
            if (dictDetail != null && dictDetail.getDictDataList() != null) {
                dictDetail.getDictDataList().forEach(item ->
                        codeLabelMap.put(item.getDictValue(), item.getDictLabel()));
            }
        } catch (Exception e) {
            log.warn("查询试验类型字典失败，将使用原始code值", e);
        }
        options.forEach(opt ->
                opt.setExperimentTypeName(codeLabelMap.getOrDefault(opt.getExperimentType(), opt.getExperimentType())));
        return options;
    }

    @Override
    public List<StabilityTrendParameterOptionDTO> getParameterOptions(Long versionId) {
        return stabilityTrendMapper.selectParameterOptions(versionId);
    }

    @Override
    public List<StabilityTrendDataPointOptionDTO> getDataPointOptions(Long versionId, Long parameterId) {
        return stabilityTrendMapper.selectDataPointOptions(versionId, parameterId);
    }

    @Override
    public List<StabilityTrendBatchOptionDTO> getBatchOptions(Long versionId, String experimentType, String storageCondition) {
        return stabilityTrendMapper.selectBatchOptions(versionId, experimentType, storageCondition);
    }

    @Override
    public List<StabilityTrendDataItemDTO> queryTrendData(StabilityTrendQueryDTO queryDTO) {
        return stabilityTrendMapper.selectTrendData(queryDTO);
    }

    @Override
    public StabilityTrendTableResultDTO queryTrendTable(StabilityTrendQueryDTO queryDTO) {
        StabilityTrendTableResultDTO result = new StabilityTrendTableResultDTO();

        // 基础信息
        StabilityTrendTableHeaderDTO basicInfo = stabilityTrendMapper.selectDataPointById(queryDTO.getVersionId(), queryDTO.getDataPointName());
        if (basicInfo == null) {
            basicInfo = new StabilityTrendTableHeaderDTO();
        }
        basicInfo.setExperimentType(queryDTO.getExperimentType());
        basicInfo.setExperimentTypeName(resolveExperimentTypeName(queryDTO.getExperimentType()));
        basicInfo.setStorageCondition(queryDTO.getStorageCondition());
        result.setBasicInfo(basicInfo);

        // 时间点列（从方案配置获取）
        List<StabilityTrendTimepointColumnDTO> timepointColumns = stabilityTrendMapper.selectTimepointColumns(
                queryDTO.getVersionId(), queryDTO.getExperimentType(), queryDTO.getStorageCondition());
        timepointColumns.forEach(col -> col.setLabel(buildTimeLabel(col.getTimeValue(), col.getTimeUnit())));
        result.setTimepointColumns(timepointColumns);

        // 数据行：平铺数据转换为按批次分组（0月数据优先合并）
        List<StabilityTrendDataItemDTO> flatItems = stabilityTrendMapper.selectTrendData(queryDTO);
        List<StabilityTrendDataItemDTO> zeroMonthItems = fetchZeroMonthItems(queryDTO);
        List<StabilityTrendDataItemDTO> allItems = new ArrayList<>(zeroMonthItems);
        allItems.addAll(flatItems);
        LinkedHashMap<String, StabilityTrendDataRowDTO> rowMap = new LinkedHashMap<>();
        for (StabilityTrendDataItemDTO item : allItems) {
            StabilityTrendDataRowDTO row = rowMap.computeIfAbsent(item.getBatchNo(), k -> {
                StabilityTrendDataRowDTO r = new StabilityTrendDataRowDTO();
                r.setBatchNo(k);
                r.setTimepointValues(new ArrayList<>());
                return r;
            });
            String value = item.getValueText() != null ? item.getValueText()
                    : (item.getValueNumber() != null ? item.getValueNumber().toPlainString() : null);
            StabilityTrendDataRowDTO.TimepointValue tv = new StabilityTrendDataRowDTO.TimepointValue();
            tv.setTimeValue(item.getTimeValue());
            tv.setTimeUnit(item.getTimeUnit());
            tv.setValue(value);
            row.getTimepointValues().add(tv);
        }
        result.setDataRows(new ArrayList<>(rowMap.values()));
        return result;
    }

    @Override
    public StabilityTrendChartResultDTO queryTrendChart(StabilityTrendQueryDTO queryDTO) {
        List<StabilityTrendDataItemDTO> items = stabilityTrendMapper.selectTrendData(queryDTO);
        StabilityTrendChartResultDTO result = new StabilityTrendChartResultDTO();
        if (items.isEmpty()) {
            result.setXAxis(Collections.emptyList());
            result.setSeries(Collections.emptyList());
            return result;
        }

        // Build ordered distinct timepoints (data is already sorted by timeValue from SQL)
        LinkedHashMap<String, StabilityTrendChartXAxisDTO> xAxisMap = new LinkedHashMap<>();
        for (StabilityTrendDataItemDTO item : items) {
            String key = item.getTimeValue() + "_" + item.getTimeUnit();
            xAxisMap.computeIfAbsent(key, k -> {
                StabilityTrendChartXAxisDTO axis = new StabilityTrendChartXAxisDTO();
                axis.setTimeValue(item.getTimeValue());
                axis.setTimeUnit(item.getTimeUnit());
                axis.setLabel(buildTimeLabel(item.getTimeValue(), item.getTimeUnit()));
                return axis;
            });
        }
        List<String> xAxisKeys = new ArrayList<>(xAxisMap.keySet());
        int xAxisSize = xAxisKeys.size();

        // Build series: one per batchNo, data array aligned to xAxis (null for missing timepoints)
        LinkedHashMap<String, List<String>> seriesDataMap = new LinkedHashMap<>();
        for (StabilityTrendDataItemDTO item : items) {
            seriesDataMap.computeIfAbsent(item.getBatchNo(),
                    k -> new ArrayList<>(Collections.nCopies(xAxisSize, null)));
            int idx = xAxisKeys.indexOf(item.getTimeValue() + "_" + item.getTimeUnit());
            String value = item.getValueText() != null ? item.getValueText()
                    : (item.getValueNumber() != null ? item.getValueNumber().toPlainString() : null);
            seriesDataMap.get(item.getBatchNo()).set(idx, value);
        }

        List<StabilityTrendChartSeriesDTO> series = seriesDataMap.entrySet().stream()
                .map(e -> {
                    StabilityTrendChartSeriesDTO s = new StabilityTrendChartSeriesDTO();
                    s.setBatchNo(e.getKey());
                    s.setData(e.getValue());
                    return s;
                })
                .collect(Collectors.toList());

        result.setXAxis(new ArrayList<>(xAxisMap.values()));
        result.setSeries(series);
        return result;
    }

    private String buildTimeLabel(Integer timeValue, String timeUnit) {
        if (timeValue == null) return "";
        if ("MONTH".equals(timeUnit)) return timeValue + "月";
        if ("YEAR".equals(timeUnit)) return timeValue + "年";
        if ("DAY".equals(timeUnit)) return timeValue + "天";
        return timeValue + (timeUnit != null ? timeUnit : "");
    }

    private List<StabilityTrendDataItemDTO> fetchZeroMonthItems(StabilityTrendQueryDTO queryDTO) {
        List<StabilityInspectPlan> plans = planMapper.selectList(
                new LambdaQueryWrapperX<StabilityInspectPlan>()
                        .eq(StabilityInspectPlan::getSchemeVersionId, queryDTO.getVersionId()));
        if (plans == null || plans.isEmpty()) {
            return Collections.emptyList();
        }
        List<StabilityTrendDataItemDTO> result = new ArrayList<>();
        Set<String> processedBatchNos = new HashSet<>();
        for (StabilityInspectPlan plan : plans) {
            List<StabilityInspectPlanBatch> batches = batchMapper.selectByPlanId(plan.getId());
            if (batches == null || batches.isEmpty()) continue;
            for (StabilityInspectPlanBatch batch : batches) {
                String batchNo = batch.getBatchNo();
                if (queryDTO.getBatchNo() != null && !queryDTO.getBatchNo().isEmpty()
                        && !queryDTO.getBatchNo().equals(batchNo)) {
                    continue;
                }
                if (!processedBatchNos.add(batchNo)) continue;
                List<InspectionOrder> orders = inspectionOrderMapper.selectByBatchNoAndMaterialId(
                        batchNo, plan.getMaterialId());
                if (orders == null || orders.isEmpty()) continue;
                Long targetOrderId;
                if (orders.size() == 1) {
                    targetOrderId = orders.get(0).getId();
                } else {
                    if (batch.getZeroMonthOrderId() == null) continue;
                    targetOrderId = batch.getZeroMonthOrderId();
                }
                result.addAll(stabilityTrendMapper.selectZeroMonthEntryRecords(
                        targetOrderId, queryDTO.getDataPointName(), batchNo));
            }
        }
        return result;
    }

    private String resolveExperimentTypeName(String experimentType) {
        if (experimentType == null) return null;
        try {
            DictDetailFeignVO dictDetail = FeignUtils.handleRequest(
                    data -> dictFeign.selectDictDetailByCode(data), DictCodeConstant.STABILITY_EXPERIMENT_TYPE).getData();
            if (dictDetail != null && dictDetail.getDictDataList() != null) {
                return dictDetail.getDictDataList().stream()
                        .filter(item -> experimentType.equals(item.getDictValue()))
                        .map(item -> item.getDictLabel())
                        .findFirst()
                        .orElse(experimentType);
            }
        } catch (Exception e) {
            log.warn("查询试验类型字典失败", e);
        }
        return experimentType;
    }

    @Override
    public void exportTrendData(StabilityTrendQueryDTO queryDTO, HttpServletResponse response) {
        try {
            List<StabilityTrendDataItemDTO> items = stabilityTrendMapper.selectTrendData(queryDTO);

            // 按SQL返回顺序收集不重复时间点（已按 batch_no, time_value 排序）
            LinkedHashMap<String, String> tpLabelMap = new LinkedHashMap<>();
            for (StabilityTrendDataItemDTO item : items) {
                String key = item.getTimeValue() + "_" + item.getTimeUnit();
                tpLabelMap.computeIfAbsent(key, k -> buildTimeLabel(item.getTimeValue(), item.getTimeUnit()));
            }
            List<String> tpKeys = new ArrayList<>(tpLabelMap.keySet());
            Map<String, Integer> tpIndex = new LinkedHashMap<>();
            for (int i = 0; i < tpKeys.size(); i++) tpIndex.put(tpKeys.get(i), i);

            // 表头：批号 | 0月 | 3月 | ...
            List<List<String>> head = new ArrayList<>();
            head.add(Collections.singletonList("批号"));
            tpLabelMap.values().forEach(label -> head.add(Collections.singletonList(label)));

            // 数据行：每批号一行，列值对齐时间点
            LinkedHashMap<String, List<Object>> rowMap = new LinkedHashMap<>();
            for (StabilityTrendDataItemDTO item : items) {
                rowMap.computeIfAbsent(item.getBatchNo(), k -> {
                    List<Object> row = new ArrayList<>(Collections.nCopies(1 + tpKeys.size(), null));
                    row.set(0, item.getBatchNo());
                    return row;
                });
                int col = 1 + tpIndex.get(item.getTimeValue() + "_" + item.getTimeUnit());
                String value = item.getValueText() != null ? item.getValueText()
                        : (item.getValueNumber() != null ? item.getValueNumber().toPlainString() : null);
                rowMap.get(item.getBatchNo()).set(col, value);
            }

            setExcelResponse(response, "稳定性趋势查询");
            EasyExcel.write(response.getOutputStream())
                    .head(head)
                    .sheet("稳定性趋势查询")
                    .doWrite(new ArrayList<>(rowMap.values()));
        } catch (Exception e) {
            log.error("导出稳定性趋势查询数据失败", e);
            throw new RuntimeException("导出失败，请稍后重试");
        }
    }

    private static void setExcelResponse(HttpServletResponse response, String filename) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encoded = java.net.URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encoded + ".xlsx");
    }
}
