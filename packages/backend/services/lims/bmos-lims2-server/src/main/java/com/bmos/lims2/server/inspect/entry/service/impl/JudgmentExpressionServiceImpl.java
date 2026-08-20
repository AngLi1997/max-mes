package com.bmos.lims2.server.inspect.entry.service.impl;

import com.bmos.lims2.common.enums.CompareOperatorEnum;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.JudgmentTypeEnum;
import com.alibaba.fastjson.JSON;
import com.bmos.lims2.server.inspect.entry.dto.InspectionEntryRecordDTO;
import com.bmos.lims2.server.inspect.entry.service.JudgmentExpressionService;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeJudgmentMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeJudgmentService;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemeJudgmentDTO;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeJudgmentMapper;
import com.bmos.lims2.server.stability.scheme.mapper.StabilitySchemeParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 判定表达式评估服务实现类
 *
 * @author system
 * @since 2025/01/30
 */
@Service
public class JudgmentExpressionServiceImpl implements JudgmentExpressionService {


    @Autowired
    private InspectionSchemeParameterMapper inspectionSchemeParameterMapper;

    @Autowired
    private InspectionSchemeJudgmentMapper inspectionSchemeJudgmentMapper;

    @Autowired
    private InspectionSchemeJudgmentService inspectionSchemeJudgmentService;

    @Autowired
    private StabilitySchemeParameterMapper stabilitySchemeParameterMapper;

    @Autowired
    private StabilitySchemeJudgmentMapper stabilitySchemeJudgmentMapper;

    @Override
    public Boolean evaluateJudgmentExpression(Long taskId, List<InspectionEntryRecordDTO> entryRecords) {
        if (entryRecords == null || entryRecords.isEmpty()) {
            return true; // 默认通过
        }

        // 获取最终判定表达式
        InspectionEntryRecordDTO firstRecord = entryRecords.get(0);
        String finalExpression = getJudgmentExpression(firstRecord.getParameterId(), firstRecord.getSchemeVersionId());

        if (finalExpression == null || finalExpression.trim().isEmpty()) {
            return true; // 没有配置表达式，默认通过
        }

        try {
            // 1) 以方案数据点配置ID建立映射，便于按规则定位
            Map<Long, InspectionEntryRecordDTO> configIdToRecord = new HashMap<>();
            for (InspectionEntryRecordDTO r : entryRecords) {
                if (r.getDataPointConfigId() != null) {
                    configIdToRecord.put(r.getDataPointConfigId(), r);
                }
            }

            // 2) 获取该分析项配置下的判定规则（优先常规方案，无则尝试稳定性方案）
            Long parameterConfigId = firstRecord.getParameterConfigId();
            List<InspectionSchemeJudgmentDTO> judgments = inspectionSchemeJudgmentMapper.listByParameterConfigId(parameterConfigId);

            if (judgments == null || judgments.isEmpty()) {
                // 稳定性任务：从 lm_stability_scheme_judgment 加载并转换
                List<StabilitySchemeJudgmentDTO> stabilityJudgments = stabilitySchemeJudgmentMapper.listByParamConfigId(parameterConfigId);
                if (stabilityJudgments != null && !stabilityJudgments.isEmpty()) {
                    judgments = new java.util.ArrayList<>();
                    for (StabilitySchemeJudgmentDTO sj : stabilityJudgments) {
                        InspectionSchemeJudgmentDTO dto = new InspectionSchemeJudgmentDTO();
                        dto.setId(sj.getId());
                        dto.setParameterId(sj.getParameterId());
                        dto.setParameterConfigId(sj.getParameterConfigId());
                        dto.setDataPointConfigId(sj.getDataPointConfigId());
                        dto.setDataPointId(sj.getDataPointId());
                        dto.setPointType(sj.getPointType());
                        dto.setJudgmentType(sj.getJudgmentType());
                        dto.setDefaultResult(sj.getDefaultResult());
                        dto.setMinValue(sj.getMinValue());
                        dto.setMinOperator(sj.getMinOperator());
                        dto.setMaxValue(sj.getMaxValue());
                        dto.setMaxOperator(sj.getMaxOperator());
                        dto.setStandardValue(sj.getStandardValue());
                        dto.setExpression(sj.getExpression());
                        dto.setMinTime(sj.getMinTime());
                        dto.setMaxTime(sj.getMaxTime());
                        judgments.add(dto);
                    }
                }
            }

            // 3) 计算每条规则的布尔结果，使用 judgment.expression 作为变量名
            Map<String, Boolean> variables = new HashMap<>();
            for (InspectionSchemeJudgmentDTO j : judgments) {
                if (j.getExpression() == null || j.getExpression().trim().isEmpty()) {
                    continue;
                }
                InspectionEntryRecordDTO record = null;
                if (j.getDataPointConfigId() != null) {
                    record = configIdToRecord.get(j.getDataPointConfigId());
                }
                if (record == null && j.getDataPointId() != null) {
                    // 兜底：按原始数据点ID匹配
                    for (InspectionEntryRecordDTO r : entryRecords) {
                        if (j.getDataPointId().equals(r.getDataPointId())) {
                            record = r; break;
                        }
                    }
                }
                boolean ruleResult = evaluateSingleJudgment(j, record);
                variables.put(j.getExpression(), ruleResult);
            }

            // 4) 评估最终表达式（由判定服务完成括号、与或非等逻辑）
            return inspectionSchemeJudgmentService.evaluateJudgmentExpression(finalExpression, variables);
            
        } catch (Exception e) {
            // 表达式评估失败，记录日志并返回false
            System.err.println("判定表达式评估失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean checkAbnormalCondition(Long taskId, List<InspectionEntryRecordDTO> entryRecords) {
        if (entryRecords == null || entryRecords.isEmpty()) {
            return false;
        }

        // 检查是否有数据点值超出正常范围
        for (InspectionEntryRecordDTO record : entryRecords) {
            if (isDataPointAbnormal(record)) {
                return true;
            }
        }

        // 通过判定表达式检查
        Boolean judgmentResult = evaluateJudgmentExpression(taskId, entryRecords);
        return judgmentResult != null && !judgmentResult;
    }

    @Override
    public String getJudgmentExpression(Long parameterId, Long schemeVersionId) {
        String expr = inspectionSchemeParameterMapper.getFinalExpressionByParameterAndVersion(parameterId, schemeVersionId);
        if (expr == null || expr.trim().isEmpty()) {
            // 稳定性任务：从 lm_stability_scheme_parameter 查询
            expr = stabilitySchemeParameterMapper.getFinalExpressionByParameterAndVersion(parameterId, schemeVersionId);
        }
        return expr;
    }

    /**
     * 评估单条数据点判定规则
     */
    private boolean evaluateSingleJudgment(InspectionSchemeJudgmentDTO j, InspectionEntryRecordDTO record) {
        if (record == null) {
            return false;
        }

        DataPointTypeEnum pointType = record.getPointType();
        if (pointType == null) {
            return false;
        }

        if (JudgmentTypeEnum.RANGE.equals(j.getJudgmentType())) {
            // 数值型或时间型支持范围判定；TIME以秒为单位进行比较
            BigDecimal value = null;
            if (DataPointTypeEnum.TIME.equals(pointType)) {
                // TIME：优先使用 valueNumber（按舍入规则换算的秒值）；为空则尝试从中文单位文本解析
                if (record.getValueNumber() != null) {
                    try { value = new BigDecimal(record.getValueNumber()); } catch (Exception ignore) {}
                }
                if (value == null && record.getValueText() != null) {
                    Integer sec = com.bmos.lims2.server.eln.record.util.TimeUtil.convertToSeconds(record.getValueText());
                    if (sec != null) { value = new BigDecimal(sec); }
                }
            } else {
                if (record.getValueNumber() != null) {
                    try { value = new BigDecimal(record.getValueNumber()); } catch (Exception ignore) {}
                }
                if (value == null) {
                    try { value = record.getValueText() == null ? null : new BigDecimal(record.getValueText()); } catch (Exception ex) { value = null; }
                }
            }
            if (value == null) { return false; }
            boolean lowerOk = true;
            if (j.getMinValue() != null && j.getMinOperator() != null) {
                lowerOk = compare(j.getMinValue(), value, j.getMinOperator());
            }
            boolean upperOk = true;
            if (j.getMaxValue() != null && j.getMaxOperator() != null) {
                upperOk = compare(value, j.getMaxValue(), j.getMaxOperator());
            }
            return lowerOk && upperOk;
        }

        if (JudgmentTypeEnum.EQUAL.equals(j.getJudgmentType())) {
            if (DataPointTypeEnum.NUMBER.equals(pointType) || DataPointTypeEnum.TIME.equals(pointType)) {
                BigDecimal value = null;
                if (record.getValueNumber() != null) {
                    try {
                        value = new BigDecimal(record.getValueNumber());
                    } catch (Exception ignore) {}
                }
                if (value == null) {
                    if (DataPointTypeEnum.TIME.equals(pointType) && record.getValueText() != null) {
                        Integer sec = com.bmos.lims2.server.eln.record.util.TimeUtil.convertToSeconds(record.getValueText());
                        if (sec != null) { value = new BigDecimal(sec); }
                    } else {
                        try { value = record.getValueText() == null ? null : new BigDecimal(record.getValueText()); } catch (Exception ex) { value = null; }
                    }
                }
                if (value == null) return false;
                try {
                    BigDecimal std;
                    if (DataPointTypeEnum.TIME.equals(pointType)) {
                        // TIME：标准值可能是中文单位文本或纯秒
                        Integer sec = com.bmos.lims2.server.eln.record.util.TimeUtil.convertToSeconds(j.getStandardValue());
                        std = sec != null ? new BigDecimal(sec) : new BigDecimal(j.getStandardValue());
                    } else {
                        std = new BigDecimal(j.getStandardValue());
                    }
                    return value.compareTo(std) == 0;
                } catch (Exception ex) {
                    return false;
                }
            } else {
                if (record.getValueText() == null) return false;
                return record.getValueText().equals(j.getStandardValue());
            }
        }

        // 选项型：包含/不包含
        if (JudgmentTypeEnum.CONTAINS.equals(j.getJudgmentType())) {
            String v = record.getValueText();
            if (v == null) {
                return false;
            }
            // 标准值可配置为单值或逗号分隔多值
            Set<String> stdSet = parseStandardValues(j.getStandardValue());
            if (stdSet.isEmpty()) {
                return false;
            }
            Set<String> valueSet = parseRecordValues(v);
            if (valueSet.isEmpty()) {
                return false;
            }
            // 多选场景：只要有任一选项命中标准值即视为“包含”
            return valueSet.stream().anyMatch(stdSet::contains);
        }

        if (JudgmentTypeEnum.NOT_CONTAINS.equals(j.getJudgmentType())) {
            String v = record.getValueText();
            if (v == null) {
                return true;
            }
            Set<String> stdSet = parseStandardValues(j.getStandardValue());
            if (stdSet.isEmpty()) {
                return true;
            }
            Set<String> valueSet = parseRecordValues(v);
            if (valueSet.isEmpty()) {
                return true;
            }
            // 多选场景：只要存在命中标准值的选项即为不通过
            return valueSet.stream().noneMatch(stdSet::contains);
        }

        return false;
    }

    /**
     * 将标准值字符串解析为集合，仅支持 JSON 字符串数组格式：
     * 例如：["选项1","选项2"]。解析失败或非数组格式时返回空集合。
     */
    private java.util.Set<String> parseStandardValues(String standardValue) {
        Set<String> set = new HashSet<>();
        if (standardValue == null) {
            return set;
        }
        String sv = standardValue.trim();
        if (sv.isEmpty()) {
            return set;
        }
        if (!(sv.startsWith("[") && sv.endsWith("]"))) {
            return set;
        }
        try {
            java.util.List<String> list = JSON.parseArray(sv, String.class);
            if (list != null) {
                for (String item : list) {
                    if (item != null) {
                        String t = item.trim();
                        if (!t.isEmpty()) {
                            set.add(t);
                        }
                    }
                }
            }
        } catch (Exception ignore) {
            // 非法 JSON，返回空集合
        }
        return set;
    }

    /**
     * 解析数据点实际值：支持单选文本或 JSON 数组（多选）。
     */
    private Set<String> parseRecordValues(String valueText) {
        Set<String> set = new HashSet<>();
        if (valueText == null) {
            return set;
        }
        String vt = valueText.trim();
        if (vt.isEmpty()) {
            return set;
        }
        if (vt.startsWith("[") && vt.endsWith("]")) {
            try {
                List<String> list = JSON.parseArray(vt, String.class);
                if (list != null) {
                    for (String item : list) {
                        if (item != null && !item.trim().isEmpty()) {
                            set.add(item.trim());
                        }
                    }
                }
                return set;
            } catch (Exception ignore) {
                // 解析失败则回落为单值
            }
        }
        set.add(vt);
        return set;
    }

    private boolean compare(BigDecimal left, BigDecimal right, CompareOperatorEnum op) {
        int cmp = left.compareTo(right);
        switch (op) {
            case LESS_THAN:
                return cmp < 0;
            case LESS_THAN_OR_EQUAL:
                return cmp <= 0;
            case EQUAL:
                return cmp == 0;
            case GREATER_THAN_OR_EQUAL:
                return cmp >= 0;
            case GREATER_THAN:
                return cmp > 0;
            default:
                return false;
        }
    }

    

    // 最终表达式求值逻辑委托给 InspectionSchemeJudgmentService

    /**
     * 检查数据点是否异常
     */
    private boolean isDataPointAbnormal(InspectionEntryRecordDTO record) {
        // TODO: 根据数据点配置的正常范围检查是否异常
        // 这里可以检查：
        // 1. 数值型数据点是否在min_value和max_value范围内
        // 2. 选项型数据点是否为有效选项
        // 3. 文本型数据点是否符合格式要求
        
        return false; // 临时返回false
    }
}
