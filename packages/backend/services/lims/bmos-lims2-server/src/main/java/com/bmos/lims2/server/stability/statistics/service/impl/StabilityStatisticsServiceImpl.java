package com.bmos.lims2.server.stability.statistics.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.*;
import com.bmos.lims2.common.constants.DictCodeConstant;
import com.bmos.lims2.server.platform.util.FeignUtils;
import com.bmos.lims2.server.stability.statistics.dto.*;
import com.bmos.lims2.server.stability.statistics.dto.StabilityExperimentTypeOptionDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityPlanOptionDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsItemDTO;
import com.bmos.lims2.server.stability.statistics.dto.StabilityStatisticsQueryDTO;
import com.bmos.lims2.server.stability.statistics.mapper.StabilityStatisticsMapper;
import com.bmos.lims2.server.stability.statistics.service.StabilityStatisticsService;
import com.bmos.platform.facade.dict.feign.DictFeign;
import com.bmos.platform.facade.dict.vo.DictDetailFeignVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 稳定性统计查询ServiceImpl
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StabilityStatisticsServiceImpl implements StabilityStatisticsService {

    private final StabilityStatisticsMapper stabilityStatisticsMapper;
    private final DictFeign dictFeign;

    @Override
    public List<StabilityStatisticsItemDTO> queryStatisticsItems(StabilityStatisticsQueryDTO queryDTO) {
        return stabilityStatisticsMapper.selectStatisticsItems(queryDTO);
    }

    @Override
    public List<StabilityPlanOptionDTO> getPlanOptions(Long materialId) {
        return stabilityStatisticsMapper.selectPlanOptions(materialId);
    }

    @Override
    public List<StabilityExperimentTypeOptionDTO> getExperimentTypeOptions(Long planId) {
        List<StabilityExperimentTypeOptionDTO> options = stabilityStatisticsMapper.selectExperimentTypeOptions(planId);
        if (options == null || options.isEmpty()) {
            return options;
        }
        // 查字典，构建 dictValue -> dictLabel 映射以填充试验类型名称
        Map<String, String> codeLabelMap = new HashMap<>();
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
    public void exportStatisticsItems(StabilityStatisticsQueryDTO queryDTO, HttpServletResponse response) {
        try {
            StabilityStatisticsBasicInfoDTO basicInfo = stabilityStatisticsMapper.selectBasicInfo(queryDTO);
            List<StabilityStatisticsItemDTO> items = stabilityStatisticsMapper.selectStatisticsItems(queryDTO);

            // 构建有序数据点结构（与queryStatisticsResult保持一致）
            LinkedHashMap<Long, String> paramNames = new LinkedHashMap<>();
            LinkedHashMap<Long, Long> dpToParam = new LinkedHashMap<>();
            LinkedHashMap<Long, String> dpNames = new LinkedHashMap<>();
            for (StabilityStatisticsItemDTO item : items) {
                paramNames.putIfAbsent(item.getParameterId(), item.getParameterName());
                dpToParam.putIfAbsent(item.getDataPointId(), item.getParameterId());
                dpNames.putIfAbsent(item.getDataPointId(), item.getDataPointName());
            }
            List<Long> dpOrder = new ArrayList<>(dpNames.keySet());
            Map<Long, Integer> dpIndex = new LinkedHashMap<>();
            for (int i = 0; i < dpOrder.size(); i++) dpIndex.put(dpOrder.get(i), i);

            // 2级表头：批号/批号, 储存时间/储存时间, 参数名/数据点名, ...
            List<List<String>> head = new ArrayList<>();
            head.add(Arrays.asList("批号", "批号"));
            head.add(Arrays.asList("储存时间", "储存时间"));
            for (Long dpId : dpOrder) {
                head.add(Arrays.asList(paramNames.get(dpToParam.get(dpId)), dpNames.get(dpId)));
            }

            // 每行对应一个 批号+时间点 组合
            LinkedHashMap<String, List<Object>> rowMap = new LinkedHashMap<>();
            for (StabilityStatisticsItemDTO item : items) {
                String rowKey = item.getBatchNo() + "_" + item.getTimeValue() + item.getTimeUnit();
                rowMap.computeIfAbsent(rowKey, k -> {
                    List<Object> row = new ArrayList<>(Collections.nCopies(2 + dpOrder.size(), null));
                    row.set(0, item.getBatchNo());
                    row.set(1, buildTimeLabel(item.getTimeValue(), item.getTimeUnit()));
                    return row;
                });
                int col = 2 + dpIndex.get(item.getDataPointId());
                String value = item.getValueText() != null ? item.getValueText()
                        : (item.getValueNumber() != null ? item.getValueNumber().toPlainString() : null);
                rowMap.get(rowKey).set(col, value);
            }

            setExcelResponse(response, "稳定性统计查询");
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                    .registerWriteHandler(new BasicInfoLabelCellStyleHandler())
                    .build();
            try {
                WriteSheet sheet = EasyExcel.writerSheet("稳定性统计查询").build();

                // 顶部基础信息区：左右结构（字段名-字段值）
                String experimentTypeName = resolveExperimentTypeName(queryDTO.getExperimentType());
                List<List<Object>> basicRows = Arrays.asList(
                        Arrays.asList(
                                "检品名称", basicInfo != null ? basicInfo.getMaterialName() : null,
                                "检品编码", basicInfo != null ? basicInfo.getMaterialCode() : null,
                                "检品规格", basicInfo != null ? basicInfo.getMaterialSpec() : null
                        ),
                        Arrays.asList(
                                "试验类型", experimentTypeName,
                                "贮存条件", queryDTO.getStorageCondition(),
                                "稳定性方案", basicInfo != null ? basicInfo.getSchemeName() : null
                        )
                );
                WriteTable basicTable = EasyExcel.writerTable(0).needHead(false).build();
                excelWriter.write(basicRows, sheet, basicTable);

                // 明细区：两级动态表头
                WriteTable detailTable = EasyExcel.writerTable(1).head(head).build();
                excelWriter.write(new ArrayList<>(rowMap.values()), sheet, detailTable);
            } finally {
                excelWriter.finish();
            }
        } catch (Exception e) {
            log.error("导出稳定性统计查询数据失败", e);
            throw new RuntimeException("导出失败，请稍后重试");
        }
    }

    private static String buildTimeLabel(Integer timeValue, String timeUnit) {
        if (timeValue == null) return "";
        if ("MONTH".equals(timeUnit)) return timeValue + "月";
        if ("YEAR".equals(timeUnit)) return timeValue + "年";
        if ("DAY".equals(timeUnit)) return timeValue + "天";
        return timeValue + (timeUnit != null ? timeUnit : "");
    }

    private static void setExcelResponse(HttpServletResponse response, String filename) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encoded = java.net.URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encoded + ".xlsx");
    }

    private static class BasicInfoLabelCellStyleHandler implements CellWriteHandler {

        private static final Set<Integer> LABEL_COL_INDEX = new HashSet<>(Arrays.asList(0, 2, 4));

        @Override
        public void afterCellDispose(WriteSheetHolder writeSheetHolder,
                                     WriteTableHolder writeTableHolder,
                                     List<WriteCellData<?>> cellDataList,
                                     Cell cell,
                                     Head head,
                                     Integer relativeRowIndex,
                                     Boolean isHead) {
            if (Boolean.TRUE.equals(isHead)) {
                return;
            }
            if (cell.getRowIndex() > 1 || !LABEL_COL_INDEX.contains(cell.getColumnIndex())) {
                return;
            }

            Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
            CellStyle style = workbook.createCellStyle();
            style.cloneStyleFrom(cell.getCellStyle());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());

            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
    }

    @Override
    public StabilityStatisticsResultDTO queryStatisticsResult(StabilityStatisticsQueryDTO queryDTO) {
        StabilityStatisticsResultDTO result = new StabilityStatisticsResultDTO();

        // 1. 查询基本信息
        StabilityStatisticsBasicInfoDTO basicInfo = stabilityStatisticsMapper.selectBasicInfo(queryDTO);

        // 2. 查询明细数据
        List<StabilityStatisticsItemDTO> items = stabilityStatisticsMapper.selectStatisticsItems(queryDTO);

        // 3. 构建表头
        StabilityStatisticsHeaderDTO header = new StabilityStatisticsHeaderDTO();
        if (basicInfo != null) {
            header.setMaterialName(basicInfo.getMaterialName());
            header.setMaterialCode(basicInfo.getMaterialCode());
            header.setMaterialSpec(basicInfo.getMaterialSpec());
            header.setSchemeName(basicInfo.getSchemeName());
            header.setSchemeId(basicInfo.getSchemeId());
        }
        header.setExperimentType(queryDTO.getExperimentType());
        header.setExperimentTypeName(resolveExperimentTypeName(queryDTO.getExperimentType()));
        header.setStorageCondition(queryDTO.getStorageCondition());

        if (items == null || items.isEmpty()) {
            header.setParameters(Collections.emptyList());
            result.setHeader(header);
            result.setData(Collections.emptyList());
            return result;
        }

        // 4. 构建分析项-数据点表头
        Map<String, StabilityStatisticsParameterHeaderDTO> paramMap = new LinkedHashMap<>();
        for (StabilityStatisticsItemDTO item : items) {
            String parameterKey = item.getParameterId() + "_" + item.getSchemeVersionId();
            StabilityStatisticsParameterHeaderDTO param = paramMap.computeIfAbsent(parameterKey, k -> {
                StabilityStatisticsParameterHeaderDTO p = new StabilityStatisticsParameterHeaderDTO();
                p.setParameterId(item.getParameterId());
                p.setSchemeVersionId(item.getSchemeVersionId());
                p.setParameterCode(item.getParameterCode());
                p.setParameterName(item.getParameterName());
                p.setDataPoints(new ArrayList<>());
                return p;
            });

            boolean exists = param.getDataPoints().stream()
                .anyMatch(dp -> dp.getDataPointId().equals(item.getDataPointId()));
            if (!exists) {
                StabilityStatisticsDataPointHeaderDTO dp = new StabilityStatisticsDataPointHeaderDTO();
                dp.setDataPointId(item.getDataPointId());
                dp.setDataPointName(item.getDataPointName());
                dp.setPointType(item.getPointType());
                param.getDataPoints().add(dp);
            }
        }
        header.setParameters(new ArrayList<>(paramMap.values()));

        // 5. 构建数据行（外层按批号，内层按时间点）
        Map<String, StabilityStatisticsDataRowDTO> batchMap = new LinkedHashMap<>();
        // 内层 key: batchNo_timeValue+timeUnit -> timepointRow
        Map<String, StabilityStatisticsTimepointRowDTO> timepointMap = new LinkedHashMap<>();
        for (StabilityStatisticsItemDTO item : items) {
            String batchKey = item.getBatchNo();
            String rowKey = item.getBatchNo() + "_" + item.getTimeValue() + item.getTimeUnit();

            StabilityStatisticsDataRowDTO batchRow = batchMap.computeIfAbsent(batchKey, k -> {
                StabilityStatisticsDataRowDTO r = new StabilityStatisticsDataRowDTO();
                r.setBatchNo(item.getBatchNo());
                r.setRows(new ArrayList<>());
                return r;
            });

            StabilityStatisticsTimepointRowDTO tpRow = timepointMap.computeIfAbsent(rowKey, k -> {
                StabilityStatisticsTimepointRowDTO tp = new StabilityStatisticsTimepointRowDTO();
                tp.setTimeValue(item.getTimeValue());
                tp.setTimeUnit(item.getTimeUnit());
                tp.setDataPointValues(new HashMap<>());
                batchRow.getRows().add(tp);
                return tp;
            });

            String value = item.getValueText() != null ? item.getValueText() :
                (item.getValueNumber() != null ? item.getValueNumber().toString() : "");
            tpRow.getDataPointValues().put(item.getDataPointId(), value);
        }

        result.setHeader(header);
        result.setData(new ArrayList<>(batchMap.values()));
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
}
