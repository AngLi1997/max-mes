package com.bmos.mes.common.model.component;


import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.ScopeCompareResultEnum;
import com.bmos.mes.common.enums.record.ScopeLimitTypeEnum;
import com.bmos.mes.common.model.execute.ExecuteFormDataTimeExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阈值设置配置
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScopeLimitConfig {

    @Data
    @ApiModel("限定范围")
    public static class Scope {
        /**
         * {@link com.bmos.mes.common.enums.record.ScopeCompareResultEnum}
         */
        @ApiModelProperty(value = "上限值比较")
        private Integer upperLimit;

        /**
         * {@link com.bmos.mes.common.enums.record.ScopeCompareResultEnum}
         */
        @ApiModelProperty(value = "下限值比较")
        private Integer lowerLimit;

        @ApiModelProperty("最大值")
        private String scopeMax;

        @ApiModelProperty("最小值")
        private String scopeMin;
    }

    /**
     * 组件值结果类型 数值/时间
     */
    private String componentResultType;

    /**
     * 阈值是否自动记录
     */
    private Boolean waringAutoRecord;

    /**
     * 阈值范围
     */
    private Scope scope;

    /**
     * 限制方式
     * {@link ScopeLimitTypeEnum}
     */
    private String limit;

    /**
     * 数值等于值
     */
    private String numericalValue;


    /**
     * 范围限制描述
     */
    private static final String scope_description_i18n = "记录值'{}'(阈值设置为限制方式：范围限制,范围是'{}')";

    /**
     * 数值相等描述
     */
    private static final String equals_description_i18n = "记录值'{}'(阈值设置为限制方式：数值相等,数值是['{}'])";

    private static final String SCOPE_LIMIT_DESCRIPTION_I18N_CODE = "SCOPE_LIMIT_EXCEPTION_DESCRIPTION";
    private static final String EQUALS_DESCRIPTION_I18N_CODE = "EQUALS_EXCEPTION_DESCRIPTION";

    /**
     * 时间阈值
     */
    private static final String TIME = "TIME";

    /**
     * 数字阈值
     */
    private static final String NUMBER = "NUMBER";

    /**
     * 业务自定义组件配置
     */
    private String componentDetail;

    public ComponentDetail getComponentDetail() {
        return JsonUtils.parseObject(componentDetail, ComponentDetail.class);
    }

    /**
     * 校验配置是否齐全
     *
     * @return
     */
    private Boolean validateConfigComplete() {
        if (limit == null) {
            return false;
        }
        if (Objects.equals(limit, ScopeLimitTypeEnum.NUMBER_EQUALS.getValue()) && StrUtil.isBlank(numericalValue)) {
            return false;
        }
        if (Objects.equals(limit, ScopeLimitTypeEnum.SCOPE_LIMIT.getValue()) && scope == null) {
            return false;
        }
        if (scope != null && scope.lowerLimit == null && scope.upperLimit == null) {
            return false;
        }
        return true;
    }


    /**
     * 检查范围限制
     *
     * @param value          填报值
     * @param valueExtension 拓展值 注意:拓展值来自于前端存的或者时间差公式计算的valueExtension内的秒值
     * @return 若超限则返回异常描述记录
     * 当计算error时需要记录异常
     */
    public String checkScopeLimit(String value, String valueExtension) {
        // 校验配置是否齐全
        if (!validateConfigComplete()) {
            return null;
        }
        // 检查是否超限
        BigDecimal compareValue;
        boolean timeLimit =Objects.equals(TIME, componentResultType);
        if (timeLimit) {
            ExecuteFormDataTimeExtInfo ext = JsonUtils.parseObject(valueExtension, ExecuteFormDataTimeExtInfo.class);
            if (ext == null || StrUtil.isEmpty(ext.getTimeSeconds())) {
                return getResultDescription(value);
            }
            compareValue = new BigDecimal(ext.getTimeSeconds());
        } else {
            if (!NumberUtil.isNumber(value)) {
                return getResultDescription(value);
            }
            compareValue = new BigDecimal(value);
        }
        if (checkInScope(compareValue, timeLimit)) {
            return null;
        }
        return getResultDescription(value);
    }


    /**
     * 检查数值阈值限定
     * @param value
     * @return
     */
    public String checkNumberScopeLimit(String value) {
        // 校验配置是否齐全
        if (!validateConfigComplete()) {
            return null;
        }
        // 检查是否超限
        BigDecimal compareValue;
        boolean timeLimit =Objects.equals(TIME, componentResultType);
        if (timeLimit) {
            return null;
        }
        if (!NumberUtil.isNumber(value)) {
            return getResultDescription(value);
        }
        compareValue = new BigDecimal(value);
        if (checkInScope(compareValue, false)) {
            return null;
        }
        return getResultDescription(value);

    }

    /**
     * 检查是否在阈值范围内
     * @param compareValue
     * @param timeLimit
     * @return 在范围内返回true
     *         否则返回false
     */
    private boolean checkInScope(BigDecimal compareValue, boolean timeLimit) {
        if (Objects.equals(limit, ScopeLimitTypeEnum.NUMBER_EQUALS.getValue())) {
            return compareValue.compareTo(new BigDecimal(numericalValue)) == 0;
        } else if (Objects.equals(limit, ScopeLimitTypeEnum.SCOPE_LIMIT.getValue())) {
            boolean upperLimit = timeLimit ? scope.upperLimit != null : StrUtil.isNotBlank(scope.scopeMax);
            boolean lowerLimit = timeLimit ? scope.lowerLimit != null : StrUtil.isNotBlank(scope.scopeMin);
            // 范围支持单边范围
            return (!upperLimit ||
                    ScopeCompareResultEnum.getResultListByUpperValue(scope.upperLimit).contains(compareValue.compareTo(new BigDecimal(scope.scopeMax))))
                    && (!lowerLimit ||
                    ScopeCompareResultEnum.getResultListByLowerValue(scope.lowerLimit).contains(compareValue.compareTo(new BigDecimal(scope.scopeMin))));
        }
        return false;
    }

    private String getResultDescription(String value) {
        ScopeLimitTypeEnum limitTypeEnum = ScopeLimitTypeEnum.getEnumByValue(limit);
        switch (limitTypeEnum) {
            case SCOPE_LIMIT:
                return StrUtil.format(I18nUtils.getCodeMessage(SCOPE_LIMIT_DESCRIPTION_I18N_CODE, scope_description_i18n, null),
                        value, getLeftParentheses(), getScopeFormat(limitTypeEnum), getRightParentheses());
            case NUMBER_EQUALS:
                return StrUtil.format(I18nUtils.getCodeMessage(EQUALS_DESCRIPTION_I18N_CODE, equals_description_i18n, null),
                        value, getScopeFormat(limitTypeEnum));
            default:
                return null;
        }
    }

    private String getLeftParentheses() {
        if (StrUtil.isEmpty(scope.scopeMin)) {
            return "(";
        }
        if (Objects.equals(scope.lowerLimit, ScopeCompareResultEnum.GREATER_AND_EQUAL.getLowerLimitValue())) {
            return "[";
        } else {
            return "(";
        }
    }

    private String getRightParentheses() {
        if (StrUtil.isEmpty(scope.scopeMax)) {
            return ")";
        }
        if (Objects.equals(scope.upperLimit, ScopeCompareResultEnum.LESS_AND_EQUAL.getUpperLimitValue())) {
            return "]";
        } else {
            return ")";
        }
    }

    private String getScopeFormat(ScopeLimitTypeEnum limitTypeEnum) {
        switch (limitTypeEnum) {
            case NUMBER_EQUALS:
                if (Objects.equals(TIME, componentResultType)) {
                    return TimeUtil.convertSecondsToString(Integer.parseInt(numericalValue));
                }
                return numericalValue;
            case SCOPE_LIMIT:
                if (Objects.equals(TIME, componentResultType)) {
                    return (Objects.isNull(scope.lowerLimit) ? "-∞" : TimeUtil.convertSecondsToString(Integer.parseInt(scope.scopeMin)))
                            + StrUtil.COMMA +
                            (Objects.isNull(scope.upperLimit) ? "+∞" : TimeUtil.convertSecondsToString(Integer.parseInt(scope.scopeMax)));
                }
                return (StrUtil.isEmpty(scope.scopeMin) ? "-∞" : scope.scopeMin)
                        + StrUtil.COMMA +
                        (StrUtil.isEmpty(scope.scopeMax) ? "+∞" : scope.scopeMax);
        }
        return StrUtil.EMPTY;
    }

}
