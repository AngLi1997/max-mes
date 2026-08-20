package com.bmos.mes.service.record.enums;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.mes.common.enums.record.BasicComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.model.execute.ExecuteFormDataTimeExtInfo;
import com.bmos.mes.common.utils.BigDecimalUtil;
import com.bmos.mes.service.execute.model.calculate.CalculateParam;
import com.bmos.mes.service.execute.model.calculate.CalculateResult;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.formula.ComponentFormulaConfig;
import com.bmos.mes.service.record.model.formula.DateCalculateConfig;
import com.bmos.mes.service.record.model.formula.NumericalJudgmentConfig;
import com.bmos.mes.service.utils.DateCalculateVO;
import com.bmos.mes.service.utils.ExecuteDateCalculateUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 组件公式类型枚举
 */
@Getter
@AllArgsConstructor
@Slf4j
public enum ComponentFormulaTypeEnum implements CommonEnum<String> {
    EXPRESSION("0", "表达式", "0", true, (batchRecordComponent, keys, params, function) -> {
        String plainString = function.apply(batchRecordComponent.getFormulaExpression(), keys,
                params.stream().map(e -> (e == null || StrUtil.isEmpty(e.getValue())) ? "0" :
                        BigDecimalUtil.toBigDecimalStr(e.getValue())).collect(Collectors.toList())).toPlainString();
        return new CalculateResult(batchRecordComponent, getEvaluateRoundingResult(plainString, batchRecordComponent, false));
    }
            ,
            false, null, null),

    ASSOCIATED_REFERENCES("2", "关联引用公式", null, false,
            (batchRecordComponent, keys, values, function) -> values.stream()
                    .filter(e -> Objects.nonNull(e) && StrUtil.isNotEmpty(e.getValue()))
                    .findFirst()
                    .map(e -> {
                        if (BooleanUtil.isTrue(e.getEmptyValue())) {
                            return new CalculateResult(batchRecordComponent, e.getValue()).emptyValue(true);
                        }
                        ComponentFormulaConfig formulaConfig = batchRecordComponent.getFormulaConfig();
                        if (formulaConfig != null && formulaConfig.associationPatternConfigIsComplete()) {
                            String value = formulaConfig
                                    .getAssociationPatternConfig()
                                    .calculateResult(formulaConfig.getAssociationPatternConfig().isNumberType() ?
                                            e.getValue() : e.getExtInfo());
                            return new CalculateResult(batchRecordComponent, value, e.getExtInfo());
                        }
                        return new CalculateResult(batchRecordComponent, e.getValue(), e.getExtInfo());
                    }).orElse(null), false
            , "TEXT",
            new ArrayList<ParseKV>() {{
                add(new ParseKV("2", null));
            }}),

    TIME_DIFF_FORMULA("3", "时间差公式", null, false, (batchRecordComponent, keys, values, function) -> {
        if (CollUtil.isEmpty(values)) {
            return null;
        }
        // 不过滤空拓展值
        List<CalculateParam> collect = values.stream().filter(e -> {
            return Objects.nonNull(e) && StrUtil.isNotEmpty(e.getValue());
        }).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            return null;
        }
        List<ExecuteFormDataBaseExtInfo> infos = collect.stream().map(item -> {
            ExecuteFormDataBaseExtInfo extInfo = JsonUtils.parseObject(item.getExtInfo(),
                    ExecuteFormDataBaseExtInfo.class);
            // 当存在有value无拓展值时 下面抛出异常 使计算值回显error
            if (extInfo == null || StrUtil.isEmpty(extInfo.getTimeStamp())) {
                throw new BmosException(MesResponseCode.TIME_DIFF_FORMULA_PARAM_HAS_NO_TIMESTAMP);
            }
            return extInfo;
        }).collect(Collectors.toList());
        DateCalculateVO dateCalculateVO = ExecuteDateCalculateUtil.handelDateFormula(batchRecordComponent, keys,
                CollectionUtils.convertList(infos, ExecuteFormDataBaseExtInfo::getTimeStamp));
        if (dateCalculateVO == null) {
            return null;
        }
        return new CalculateResult(batchRecordComponent, dateCalculateVO.getCalculateResult(),
                JsonUtils.toJsonString(new ExecuteFormDataTimeExtInfo(String.valueOf(dateCalculateVO.getTimeSeconds()))));
    }, false, "TIME",
            Arrays.asList(new ParseKV("开始时间", null), new ParseKV("结束时间", null))),

    SUM("4", "求和", null, true, (batchRecordComponent, keys, values, function) -> {
        if (CollUtil.isEmpty(values)) {
            return null;
        }
        return new CalculateResult(batchRecordComponent, getEvaluateRoundingResult(values.stream().filter(Objects::nonNull)
                .map(CalculateParam::getValue).filter(StrUtil::isNotEmpty).map(BigDecimalUtil::toBigDecimal)
                .reduce(BigDecimal.ZERO,
                        BigDecimal::add)
                .toPlainString(), batchRecordComponent, false));
    }, true, "NUMBER", null),
    MAX("5", "最大值", null, true, (batchRecordComponent, keys, values, function) -> {
        if (CollUtil.isEmpty(values)) {
            return null;
        }
        List<String> collect =
                values.stream().filter(Objects::nonNull).map(CalculateParam::getValue).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            return null;
        }
        return new CalculateResult(batchRecordComponent, getEvaluateRoundingResult(collect.stream()
                .map(e-> new BigDecimalStash(e, BigDecimalUtil.toBigDecimal(e)))
                .max(Comparator.comparing(BigDecimalStash::getValue))
                .get()
                .getOriginalValue(), batchRecordComponent, true));
    }, true, "NUMBER", null),
    MIN("6", "最小值", null, true, (batchRecordComponent, keys, values, function) -> {
        if (CollUtil.isEmpty(values)) {
            return null;
        }
        List<String> collect =
                values.stream().filter(Objects::nonNull).map(CalculateParam::getValue).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            return null;
        }
        return new CalculateResult(batchRecordComponent, getEvaluateRoundingResult(collect.stream()
                .map(e-> new BigDecimalStash(e, BigDecimalUtil.toBigDecimal(e)))
                .min(Comparator.comparing(BigDecimalStash::getValue))
                .get()
                .getOriginalValue(), batchRecordComponent, true));
    }, true, "NUMBER", null),
    AVG("7", "平均值", null, true, (batchRecordComponent, keys, values, function) -> {
        if (CollUtil.isEmpty(values)) {
            return null;
        }
        List<String> collect =
                values.stream().filter(Objects::nonNull).map(CalculateParam::getValue).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollUtil.isEmpty(collect)) {
            return null;
        }
        return new CalculateResult(batchRecordComponent, getEvaluateRoundingResult(collect.stream()
                .filter(Objects::nonNull)
                .map(BigDecimalUtil::toBigDecimal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(collect.size()), 20, RoundingMode.HALF_EVEN)
                .toPlainString(), batchRecordComponent, false));
    }, true, "NUMBER", null),
    SIGN("8", "关联签名公式", null, false, (batchRecordComponent, keys, values, function) -> {
        List<String> result = values.stream()
                .filter(e -> {
                    return Objects.nonNull(e)
                            && StrUtil.isNotEmpty(e.getValue())
                            && StrUtil.isNotEmpty(e.getOperationUser());
                })
                .sorted(Comparator.comparing(CalculateParam::getOperationTime))
                .collect(Collectors.toMap(
                        CalculateParam::getOperationUser,
                        o -> o,
                        (o1, o2) -> o2)) // 保留时间最新的对象
                .values()
                .stream()
                .sorted(Comparator.comparing(CalculateParam::getOperationTime))
                .map(e -> {
                    String loginName = UserUtils.getUser(e.getOperationUser()).getUserName();
                    return loginName + StrUtil.SPACE + DateUtil.format(e.getOperationTime(), e.getTimeFormat());
                })
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(result)) {
            return null;
        }
        return new CalculateResult(batchRecordComponent, StrUtil.join(StrUtil.LF, result));
    }, true, "SIGN", null),
    NUMERICAL_JUDGMENT("9", "数值判定", null, false, (batchRecordComponent, keys, values, function) -> {
        CalculateParam valueFormData = CollUtil.getFirst(values);
        if (valueFormData == null || StrUtil.isEmpty(valueFormData.getValue())) {
            return null;
        }
        ComponentFormulaConfig formulaConfig = batchRecordComponent.getFormulaConfig();
        if (formulaConfig == null) {
            return null;
        }
        // 选择组件配置校验
        if (!formulaConfig.numericalJudgmentConfigIsComplete()) {
            log.error("内置公式计算:记录版本{}组件{}数值判定公式配置不全,请检查配置", batchRecordComponent.getRecordVersion(),
                    batchRecordComponent.getFieldId());
            return null;
        }
        // 文字数值判定
        if (Objects.equals(batchRecordComponent.getComponentType(), BasicComponentTypeEnum.TEXT.getValue())) {
            NumericalJudgmentConfig numericalJudgmentConfig =
                    CollUtil.getFirst(formulaConfig.getNumericalJudgmentConfig());
            return new CalculateResult(batchRecordComponent,
                    numericalJudgmentConfig.judgeResult(valueFormData.getValue()));
        }
        List<NumericalJudgmentConfig> choiceConfigList = formulaConfig.getNumericalJudgmentConfig();
        // 单选数值判定
        if (Objects.equals(batchRecordComponent.getComponentType(), BasicComponentTypeEnum.RADIO.getValue())) {
            for (NumericalJudgmentConfig judge : choiceConfigList) {
                String result = judge.judgeResult(valueFormData.getValue());
                if (StrUtil.isNotEmpty(result)) {
                    return new CalculateResult(batchRecordComponent, result);
                }
            }
            // 不符合时需要填入空格覆盖旧值
            return new CalculateResult(batchRecordComponent, StrUtil.SPACE).emptyValue(true);
        }
        // 多选数值判定
        if (Objects.equals(batchRecordComponent.getComponentType(), BasicComponentTypeEnum.CHECKBOX.getValue())) {
            List<String> list = new ArrayList<>();
            for (NumericalJudgmentConfig judge : choiceConfigList) {
                String result = judge.judgeResult(valueFormData.getValue());
                if (StrUtil.isNotEmpty(result)) {
                    list.add(result);
                }
            }
            if (CollUtil.isNotEmpty(list)) {
                return new CalculateResult(batchRecordComponent, JsonUtils.toJsonString(list));
            }
            return new CalculateResult(batchRecordComponent,
                    JsonUtils.toJsonString(Collections.singletonList(StrUtil.SPACE))).emptyValue(true);
        }
        return null;
    }, false, "TEXT", new ArrayList<ParseKV>() {{
        add(new ParseKV("9", null));
    }}),
    DATE_CALCULATE("10", "日期计算公式", null, false,
            (batchRecordComponent, keys, values, function) -> {
                values = values.stream().filter(e -> {
                    return Objects.nonNull(e) && StrUtil.isNotEmpty(e.getExtInfo());
                }).collect(Collectors.toList());
                if (CollUtil.isEmpty(values)) {
                    return null;
                }
                ComponentFormulaConfig formulaConfig = batchRecordComponent.getFormulaConfig();
                if (formulaConfig == null || !formulaConfig.dateCalculateConfigIsComplete()) {
                    log.error("日期计算公式:记录版本{}组件{}日期计算公式配置不全,请检查配置", batchRecordComponent.getRecordVersion(),
                            batchRecordComponent.getFieldId());
                    return null;
                }
                DateCalculateConfig dateCalculateConfig = formulaConfig.getDateCalculateConfig();
                ExecuteFormDataBaseExtInfo extInfo = JsonUtils.parseObject(CollUtil.getFirst(values).getExtInfo(),
                        ExecuteFormDataBaseExtInfo.class);
                String res = dateCalculateConfig.calculateResult(CollUtil.getFirst(values).getExtInfo());
                if (StrUtil.isEmpty(res)) {
                    return null;
                }
                extInfo.setTimeStamp(String.valueOf(dateCalculateConfig.calculateTimeStamp(CollUtil.getFirst(values).getExtInfo())));
                return new CalculateResult(batchRecordComponent, res, JSONUtil.toJsonStr(extInfo));
            }, false
            , "DATE", new ArrayList<ParseKV>() {{
        add(new ParseKV("10", null));
    }}),
    STRING_JOIN("11", "字符串拼接公式", null, false, (batchRecordComponent, keys, values, function) -> {
        values = values.stream().filter(e -> {
                    return Objects.nonNull(e) && StrUtil.isNotEmpty(e.getValue());
                }).collect(Collectors.groupingBy(CalculateParam::getFieldId, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .sorted(Comparator.comparing(CalculateParam::getOperationTime)))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(values)) {
            return null;
        }
        ComponentFormulaConfig formulaConfig = batchRecordComponent.getFormulaConfig();
        if (formulaConfig == null || !formulaConfig.stringJoinConfigComplete()) {
            return null;
        }
        return new CalculateResult(batchRecordComponent,
                formulaConfig.getStringJoinConfig().calculateResult(CollectionUtils.convertList(values,
                        CalculateParam::getValue)));
    }, true, "TEXT", null);

    @EnumValue
    private final String value;

    private final String name;

    /**
     * 默认空值赋值
     */
    private final String defaultEmptyValue;

    /**
     * 是否需要修约
     * 已弃用
     */
    @Deprecated
    private final Boolean needRounding;

    /**
     * 处理函数
     */
    private final HandleFunction<BatchRecordComponent, CalculateResult, List<String>, List<CalculateParam>,
            CalculateFunction<String, List<String>, Integer, BigDecimal>, Integer> function;

    /**
     * 是否不定参数
     */
    private final Boolean indefiniteParam;

    private final String componentType;

    /**
     * 如果非不定参数需要返回此值控制参数
     */
    private final List<ParseKV> expressionParse;

    public static List<ComponentFormulaTypeEnum> getEnumByCalculateType(BasicComponentTypeEnum componentType) {
        if (componentType == null) {
            return new ArrayList<>();
        }
        switch (componentType) {
            case DATE:
                return CollUtil.toList(ASSOCIATED_REFERENCES, DATE_CALCULATE);
            case TEXT:
                return Arrays.stream(ComponentFormulaTypeEnum.values())
                        .filter(type -> BasicComponentTypeEnum.TEXT.getValue().equals(type.getComponentType()))
                        .collect(Collectors.toList());
            case NUMBER:
                List<ComponentFormulaTypeEnum> calculate = Arrays.stream(ComponentFormulaTypeEnum.values())
                        .filter(type -> BasicComponentTypeEnum.NUMBER.getValue().equals(type.getComponentType()))
                        .collect(Collectors.toList());
                calculate.add(ASSOCIATED_REFERENCES);
                return calculate;
            case TIME:
                return Arrays.stream(ComponentFormulaTypeEnum.values())
                        .filter(type -> BasicComponentTypeEnum.TIME.getValue().equals(type.getComponentType()))
                        .collect(Collectors.toList());
            case SUBMIT_SIGN:
                return Collections.singletonList(SIGN);
            case RADIO:
            case CHECKBOX:
                return Collections.singletonList(NUMERICAL_JUDGMENT);
        }
        return new ArrayList<>();
    }

    @AllArgsConstructor
    @Getter
    public static class ParseKV {
        private String key;
        private String value;
    }


    /**
     * @param <B> BatchRecordComponent
     * @param <R> CalculateResult
     * @param <P> String集合
     * @param <V> CalculateParam集合
     * @param <C> Calculate
     * @param <S> Integer
     */
    @FunctionalInterface
    public interface HandleFunction<B, R, P, V, C, S> {
        R apply(B b, P p, V v, C c);

    }

    @FunctionalInterface
    public interface CalculateFunction<E, K, S, R> {
        R apply(E e, K k, K v);
    }

    /**
     * @param value 若无对应类型 默认返回表达式计算
     * @return
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ComponentFormulaTypeEnum getEnumByValue(String value) {
        return Arrays.stream(ComponentFormulaTypeEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(EXPRESSION);
    }

    /**
     * 计算公式修约
     * 配置修约规则与精度时按照规则修约
     * 未配置时最大15位 四舍六入五成双 不补零
     * @param evaluate
     * @param batchRecordComponent
     * @param noConfigReturn 无修约配置则直接返回
     * @return
     */
    private static String getEvaluateRoundingResult(String evaluate, BatchRecordComponent batchRecordComponent, boolean noConfigReturn) {
        Long formulaPrecision = batchRecordComponent.getFormulaPrecision();
        if (noConfigReturn && formulaPrecision == null) {
            return evaluate;
        }
        RoundingMode roundingMode = RoundingEnum.getEnumByCode(batchRecordComponent.getRoundCode()).getMapping();

        if (formulaPrecision == null) {
            return BigDecimalUtil.toBigDecimal(evaluate).setScale(15, roundingMode).stripTrailingZeros().toPlainString();
        } else {
            return BigDecimalUtil.toBigDecimal(evaluate).setScale(formulaPrecision.intValue(), roundingMode).toPlainString();
        }
    }

    /**
     * 数值暂存 用于保留数值原格式值
     */
    @Data
    @AllArgsConstructor
    public static class BigDecimalStash {
        /**
         * 原始格式值
         */
        private String originalValue;

        /**
         * 转成BigDecimal的值
         */
        private BigDecimal value;
    }

}
