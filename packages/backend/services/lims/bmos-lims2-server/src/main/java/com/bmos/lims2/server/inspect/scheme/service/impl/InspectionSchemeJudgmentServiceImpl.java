package com.bmos.lims2.server.inspect.scheme.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.enums.DataPointTypeEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.entity.BatchRecordComponent;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordComponentMapper;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO;
import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeJudgmentDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeJudgmentBatchUpdateDTO;
import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeJudgmentSaveDTO;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeJudgment;
import com.bmos.lims2.server.inspect.scheme.entity.InspectionSchemeParameter;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeDataPointMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeJudgmentMapper;
import com.bmos.lims2.server.inspect.scheme.mapper.InspectionSchemeParameterMapper;
import com.bmos.lims2.server.inspect.scheme.service.InspectionSchemeJudgmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 检验方案判定配置Service实现类
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Service
public class InspectionSchemeJudgmentServiceImpl extends ServiceImpl<InspectionSchemeJudgmentMapper, InspectionSchemeJudgment> implements InspectionSchemeJudgmentService {

    @Autowired
    private InspectionSchemeJudgmentMapper inspectionSchemeJudgmentMapper;

    @Autowired
    private InspectionSchemeParameterMapper inspectionSchemeParameterMapper;

    @Autowired
    private InspectionSchemeDataPointMapper inspectionSchemeDataPointMapper;

    @Autowired
    private BatchRecordComponentMapper batchRecordComponentMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectionSchemeJudgments(Long schemeId, Long versionId, Long packageId, Long parameterConfigId, List<InspectionSchemeJudgmentSaveDTO> saveDTOList) {
        List<InspectionSchemeJudgmentSaveDTO> judgments = CollUtil.isEmpty(saveDTOList) ? Collections.emptyList() : saveDTOList;
        boolean hasSaveList = CollUtil.isNotEmpty(judgments);
        // 校验：判定条件引用的数据点必须存在且未被删除
        if (hasSaveList) {
            java.util.List<com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO> dpList =
                    inspectionSchemeDataPointMapper.listByParameterConfigId(parameterConfigId);
            java.util.Set<Long> aliveDpConfigIds = new java.util.HashSet<>();
            java.util.Map<Long, com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO> configIdToDp = new java.util.HashMap<>();
            boolean elnExecute = false;
            InspectionSchemeParameter parameterCfg = inspectionSchemeParameterMapper.selectById(parameterConfigId);
            if (parameterCfg != null && ExecuteMethodEnum.ELN.equals(parameterCfg.getExecuteMethod())) {
                elnExecute = true;
            }
            if (dpList != null) {
                for (com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO dp : dpList) {
                    if (dp.getId() != null) {
                        aliveDpConfigIds.add(dp.getId());
                        configIdToDp.put(dp.getId(), dp);
                    }
                }
            }
            Map<Long, Set<String>> componentOptionCache = new HashMap<>();
            for (InspectionSchemeJudgmentSaveDTO s : judgments) {
                if (s.getDataPointConfigId() != null && !aliveDpConfigIds.contains(s.getDataPointConfigId())) {
                    throw new com.bmos.common.exception.BmosException(com.bmos.lims2.common.i18n.LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_DELETED);
                }
                com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO dp = resolveDataPoint(s.getDataPointConfigId(), configIdToDp);
                s.setPointType(resolvePointType(s.getDataPointConfigId(),
                        convertTypeMap(configIdToDp), java.util.Collections.emptyMap()));
                if (elnExecute && dp != null
                        && (dp.getComponentId() == null || dp.getFieldId() == null)) {
                    throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_BINDING_MISSING, dp.getName());
                }
                validateOptionValueIfNeeded(s.getStandardValue(), dp, elnExecute, componentOptionCache);
            }
        }
        // 删除原有配置
        LambdaQueryWrapper<InspectionSchemeJudgment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeJudgment::getParameterConfigId, parameterConfigId);
        inspectionSchemeJudgmentMapper.delete(wrapper);

        if (!hasSaveList) {
            return;
        }

        // 保存新配置
        judgments.forEach(saveDTO -> {
            InspectionSchemeJudgment judgment = BeanUtil.copyProperties(saveDTO, InspectionSchemeJudgment.class);
            judgment.setDataPointId(saveDTO.getDataPointId());
            judgment.setSchemeId(schemeId);
            judgment.setVersionId(versionId);
            judgment.setPackageId(packageId);
            judgment.setParameterConfigId(parameterConfigId);
            judgment.setParameterId(saveDTO.getInspectParameterId());
            judgment.setInspectItemId(saveDTO.getInspectItemId());
            inspectionSchemeJudgmentMapper.insert(judgment);
        });
    }

    @Override
    public List<InspectionSchemeJudgmentDTO> listInspectionSchemeJudgments(Long parameterConfigId) {
        // 查询判定配置列表（包含数据点信息）
        return inspectionSchemeJudgmentMapper.listByParameterConfigId(parameterConfigId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInspectionSchemeJudgments(Long parameterConfigId) {
        // 删除判定配置
        LambdaQueryWrapper<InspectionSchemeJudgment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InspectionSchemeJudgment::getParameterConfigId, parameterConfigId);
        inspectionSchemeJudgmentMapper.delete(wrapper);
    }

    @Override
    public Boolean testJudgmentExpression(String expression, Map<String, Boolean> variables) {
        // 规范：表达式不合法时抛错，合法则返回求值结果（可能为true或false）
        if (!isValidExpression(expression)) {
            throw new BmosException(LimsResponseCode.EXPRESSION_INVALID);
        }
        try {
            String evaluatedExpression = replaceVariables(expression, variables);
            return evaluateExpression(evaluatedExpression);
        } catch (Exception ex) {
            throw new BmosException(LimsResponseCode.EXPRESSION_INVALID);
        }
    }

    @Override
    public Boolean evaluateJudgmentExpression(String expression, Map<String, Boolean> variables) {
        // 替换变量为具体值
        String evaluatedExpression = replaceVariables(expression, variables);

        // 计算表达式结果
        return evaluateExpression(evaluatedExpression);
    }

    /**
     * 检查表达式格式是否正确
     *
     * @param expression 表达式
     * @return 是否正确
     */
    private boolean isValidExpression(String expression) {
        // 检查括号是否匹配
        int count = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
                if (count < 0) {
                    return false;
                }
            }
        }
        if (count != 0) {
            return false;
        }

        // 检查运算符是否合法
        Pattern pattern = Pattern.compile("[^A-Za-z0-9\\s()&|!]");
        Matcher matcher = pattern.matcher(expression);
        if (matcher.find()) {
            return false;
        }

        return true;
    }

    /**
     * 替换变量为具体值
     *
     * @param expression 表达式
     * @param variables  变量值映射
     * @return 替换后的表达式
     */
    private String replaceVariables(String expression, Map<String, Boolean> variables) {
        String result = expression;
        for (Map.Entry<String, Boolean> entry : variables.entrySet()) {
            // 使用字面量替换，避免正则特殊字符影响
            result = result.replace(entry.getKey(), entry.getValue().toString());
        }
        return result;
    }

    /**
     * 计算表达式结果
     *
     * @param expression 表达式
     * @return 计算结果
     */
    private Boolean evaluateExpression(String expression) {
        // 去除空格
        expression = expression.replaceAll("\\s+", "");

        // 处理括号
        while (expression.contains("(")) {
            int start = expression.lastIndexOf("(");
            int end = expression.indexOf(")", start);
            String subExpression = expression.substring(start + 1, end);
            Boolean result = evaluateSimpleExpression(subExpression);
            expression = expression.substring(0, start) + result + expression.substring(end + 1);
        }

        return evaluateSimpleExpression(expression);
    }

    /**
     * 计算简单表达式结果（不包含括号）
     *
     * @param expression 表达式
     * @return 计算结果
     */
    private Boolean evaluateSimpleExpression(String expression) {
        // 使用递归下降解析器，支持 !、&& / &、|| / |、true/false
        class BooleanExprParser {
            private final String expr;
            private int pos = 0;

            BooleanExprParser(String expr) {
                this.expr = expr;
            }

            boolean parse() {
                boolean value = parseOr();
                if (pos != expr.length()) {
                    throw new IllegalArgumentException("Unexpected token at position " + pos);
                }
                return value;
            }

            private boolean parseOr() {
                boolean value = parseAnd();
                while (match("||") || match("|")) {
                    boolean right = parseAnd();
                    value = value || right;
                }
                return value;
            }

            private boolean parseAnd() {
                boolean value = parseNot();
                while (match("&&") || match("&")) {
                    boolean right = parseNot();
                    value = value && right;
                }
                return value;
            }

            private boolean parseNot() {
                int notCount = 0;
                while (match("!")) {
                    notCount++;
                }
                boolean base = parseLiteral();
                return (notCount % 2 == 0) ? base : !base;
            }

            private boolean parseLiteral() {
                if (matchIgnoreCase("true")) {
                    return true;
                }
                if (matchIgnoreCase("false")) {
                    return false;
                }
                throw new IllegalArgumentException("Expected boolean literal at position " + pos);
            }

            private boolean match(String token) {
                if (pos + token.length() <= expr.length() && expr.startsWith(token, pos)) {
                    pos += token.length();
                    return true;
                }
                return false;
            }

            private boolean matchIgnoreCase(String token) {
                int len = token.length();
                if (pos + len <= expr.length() && expr.regionMatches(true, pos, token, 0, len)) {
                    pos += len;
                    return true;
                }
                return false;
            }
        }

        return new BooleanExprParser(expression).parse();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateInspectionSchemeJudgments(Long parameterConfigId, String finalExpression, List<InspectionSchemeJudgmentBatchUpdateDTO> batchUpdateDTO) {
        // 空数组/空集合：表示删除该分析项下的所有判定条件，同时更新最终表达式
        if (batchUpdateDTO == null || batchUpdateDTO.isEmpty()) {
            // 先更新分析项的表达式
            UpdateWrapper<InspectionSchemeParameter> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda()
                    .eq(InspectionSchemeParameter::getId, parameterConfigId)
                    .set(InspectionSchemeParameter::getFinalExpression, finalExpression);
            inspectionSchemeParameterMapper.update(null, updateWrapper);

            // 删除该分析项下的所有判定条件
            LambdaQueryWrapper<InspectionSchemeJudgment> delWrapper = new LambdaQueryWrapper<>();
            delWrapper.eq(InspectionSchemeJudgment::getParameterConfigId, parameterConfigId);
            inspectionSchemeJudgmentMapper.delete(delWrapper);
            return;
        }

        // 校验：判定条件引用的数据点必须存在且未被删除（按各自的 parameterConfigId 校验）
        java.util.Set<Long> paramIds = batchUpdateDTO.stream().map(InspectionSchemeJudgmentBatchUpdateDTO::getParameterConfigId).filter(Objects::nonNull).collect(Collectors.toSet());
        java.util.Map<Long, java.util.Set<Long>> paramIdToAliveDpConfigIds = new java.util.HashMap<>();
        java.util.Map<Long, com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO> dataPointConfigMap = new java.util.HashMap<>();
        java.util.Map<Long, DataPointTypeEnum> dataPointConfigTypeMap = new java.util.HashMap<>();
        Map<Long, Boolean> paramIdToEln = new java.util.HashMap<>();
        for (Long pid : paramIds) {
            java.util.List<com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO> dpList = inspectionSchemeDataPointMapper.listByParameterConfigId(pid);
            java.util.Set<Long> cfgIds = new java.util.HashSet<>();
            if (dpList != null) {
                for (com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO dp : dpList) {
                    if (dp.getId() != null) {
                        cfgIds.add(dp.getId());
                        dataPointConfigTypeMap.put(dp.getId(), dp.getPointType());
                        dataPointConfigMap.put(dp.getId(), dp);
                    }
                }
            }
            paramIdToAliveDpConfigIds.put(pid, cfgIds);
            InspectionSchemeParameter param = inspectionSchemeParameterMapper.selectById(pid);
            paramIdToEln.put(pid, param != null && ExecuteMethodEnum.ELN.equals(param.getExecuteMethod()));
        }
        Map<Long, Set<String>> componentOptionCache = new HashMap<>();
        for (InspectionSchemeJudgmentBatchUpdateDTO j : batchUpdateDTO) {
            Long pid = j.getParameterConfigId();
            Set<Long> cfgIds = paramIdToAliveDpConfigIds.getOrDefault(pid, Collections.emptySet());
            if (j.getDataPointConfigId() != null && !cfgIds.contains(j.getDataPointConfigId())) {
                throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_DELETED);
            }
            InspectionSchemeDataPointDTO dp =
                    resolveDataPoint(j.getDataPointConfigId(), dataPointConfigMap);
            if (Boolean.TRUE.equals(paramIdToEln.get(pid)) && dp != null
                    && (dp.getComponentId() == null || dp.getFieldId() == null)) {
                throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_BINDING_MISSING, dp.getName());
            }
            j.setPointType(resolvePointType(j.getDataPointConfigId(), dataPointConfigTypeMap, Collections.emptyMap()));
            validateOptionValueIfNeeded(j.getStandardValue(), dp, Boolean.TRUE.equals(paramIdToEln.get(pid)), componentOptionCache);
        }

        // 先更新分析项的表达式
        UpdateWrapper<InspectionSchemeParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(InspectionSchemeParameter::getId, parameterConfigId).set(InspectionSchemeParameter::getFinalExpression, finalExpression);
        inspectionSchemeParameterMapper.update(null, updateWrapper);

        List<Long> parameterConfigIds = batchUpdateDTO.stream()
                .map(InspectionSchemeJudgmentBatchUpdateDTO::getParameterConfigId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 获取当前分析项配置的所有判定条件
        List<InspectionSchemeJudgment> existingJudgments = parameterConfigIds.isEmpty()
                ? java.util.Collections.emptyList()
                : list(new LambdaQueryWrapper<InspectionSchemeJudgment>()
                        .in(InspectionSchemeJudgment::getParameterConfigId, parameterConfigIds));

        // 记录前端提交的判定条件ID（用于判断哪些判定条件被删除了）
        Set<Long> submittedJudgmentIds = new HashSet<>();

        // 处理前端提交的每个判定条件
        for (InspectionSchemeJudgmentBatchUpdateDTO judgmentConfig : batchUpdateDTO) {
            if (judgmentConfig.getJudgmentConfigId() == null) {
                // 新增判定条件（没有judgmentConfigId）
                addJudgment(judgmentConfig);
            } else {
                // 更新现有判定条件
                updateJudgment(judgmentConfig);
                submittedJudgmentIds.add(judgmentConfig.getJudgmentConfigId());
            }
        }

        // 删除前端没有提交的现有判定条件（前端删除的判定条件不会传到后端）
        for (InspectionSchemeJudgment existingJudgment : existingJudgments) {
            if (!submittedJudgmentIds.contains(existingJudgment.getId())) {
                // 删除判定条件
                removeById(existingJudgment.getId());
            }
        }
    }

    /**
     * 新增判定条件
     */
    private void addJudgment(InspectionSchemeJudgmentBatchUpdateDTO judgmentConfig) {
        InspectionSchemeJudgment judgment = new InspectionSchemeJudgment();
        BeanUtil.copyProperties(judgmentConfig, judgment);
        save(judgment);
    }

    /**
     * 更新判定条件
     */
    private void updateJudgment(InspectionSchemeJudgmentBatchUpdateDTO judgmentConfig) {
        InspectionSchemeJudgment inspectionSchemeJudgment = BeanUtil.copyProperties(judgmentConfig, InspectionSchemeJudgment.class);
        inspectionSchemeJudgment.setId(judgmentConfig.getJudgmentConfigId());
        updateById(inspectionSchemeJudgment);
    }

    /**
     * 根据数据点配置或原始数据点ID解析数据点类型
     *
     * @param dataPointConfigId 数据点配置ID
     * @param configTypeMap 配置ID与类型映射
     * @param originalTypeMap 原始ID与类型映射
     * @return 数据点类型，若未匹配到则返回null
     */
    private DataPointTypeEnum resolvePointType(Long dataPointConfigId,
                                               Map<Long, DataPointTypeEnum> configTypeMap,
                                               Map<Long, DataPointTypeEnum> originalTypeMap) {
        if (dataPointConfigId != null && configTypeMap != null) {
            return configTypeMap.get(dataPointConfigId);
        }
        return null;
    }

    private Map<Long, DataPointTypeEnum> convertTypeMap(Map<Long, com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO> source) {
        if (source == null) {
            return Collections.emptyMap();
        }
        Map<Long, DataPointTypeEnum> target = new HashMap<>();
        source.forEach((k, v) -> target.put(k, v == null ? null : v.getPointType()));
        return target;
    }

    private com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeDataPointDTO resolveDataPoint(Long dataPointConfigId,
                                                                                                   Map<Long, InspectionSchemeDataPointDTO> configMap) {
        if (dataPointConfigId != null && configMap != null) {
            return configMap.get(dataPointConfigId);
        }
        return null;
    }

    /**
     * 校验选择型判定条件的标准值是否存在于组件选项中
     */
    private void validateOptionValueIfNeeded(String standardValue,
                                             InspectionSchemeDataPointDTO dp,
                                             boolean elnExecute,
                                             Map<Long, Set<String>> componentOptionCache) {
        if (!elnExecute || dp == null || dp.getPointType() != DataPointTypeEnum.OPTION) {
            return;
        }
        if (StrUtil.isBlank(standardValue)) {
            return;
        }
        if (dp.getComponentId() == null) {
            throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_BINDING_MISSING, dp.getName());
        }
        Set<String> optionFields = loadComponentOptionFields(dp.getComponentId(), componentOptionCache);
        if (CollUtil.isEmpty(optionFields)) {
            throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_OPTION_INVALID, standardValue);
        }
        List<String> values = parseStandardValues(standardValue);
        for (String value : values) {
            if (StrUtil.isBlank(value) || !optionFields.contains(value)) {
                throw new BmosException(LimsResponseCode.JUDGMENT_REFERENCE_DATA_POINT_OPTION_INVALID, value);
            }
        }
    }

    /**
     * 读取组件配置中的选项字段列表
     */
    private Set<String> loadComponentOptionFields(Long componentId, Map<Long, Set<String>> cache) {
        if (componentId == null) {
            return Collections.emptySet();
        }
        if (cache.containsKey(componentId)) {
            return cache.get(componentId);
        }
        Set<String> fields = new HashSet<>();
        BatchRecordComponent component = batchRecordComponentMapper.selectWithDetailById(componentId);
        if (component != null && StrUtil.isNotBlank(component.getComponentDetail())) {
            try {
                JSONArray array = JSON.parseArray(component.getComponentDetail());
                if (array != null) {
                    array.forEach(obj -> {
                        try {
                            String field = JSON.parseObject(JSON.toJSONString(obj)).getString("field");
                            if (StrUtil.isNotBlank(field)) {
                                fields.add(field);
                            }
                        } catch (Exception ignored) {
                            // ignore single item parse error
                        }
                    });
                }
            } catch (Exception ignored) {
                // ignore all parse error, handled by later validation
            }
        }
        cache.put(componentId, fields);
        return fields;
    }

    /**
     * 解析判定标准值，支持 JSON 数组或逗号分隔字符串
     */
    private List<String> parseStandardValues(String standardValue) {
        if (StrUtil.isBlank(standardValue)) {
            return Collections.emptyList();
        }
        try {
            JSONArray array = JSON.parseArray(standardValue);
            if (array != null) {
                List<String> values = new ArrayList<>();
                array.forEach(obj -> {
                    if (obj == null) {
                        return;
                    }
                    if (obj instanceof JSONObject) {
                        String field = ((JSONObject) obj).getString("field");
                        if (StrUtil.isNotBlank(field)) {
                            values.add(StrUtil.trim(field));
                            return;
                        }
                    }
                    values.add(StrUtil.trim(obj.toString()));
                });
                if (CollUtil.isNotEmpty(values)) {
                    return values;
                }
            }
        } catch (Exception ignored) {
            // 非 JSON 数组时，走逗号分隔解析
        }
        List<String> split = StrUtil.splitTrim(standardValue, ',');
        if (CollUtil.isEmpty(split)) {
            return Collections.singletonList(StrUtil.trim(standardValue));
        }
        return split;
    }

} 