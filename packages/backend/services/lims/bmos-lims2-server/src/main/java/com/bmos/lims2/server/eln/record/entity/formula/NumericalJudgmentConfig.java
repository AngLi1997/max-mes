package com.bmos.lims2.server.eln.record.entity.formula;

import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.ScopeCompareResultEnum;
import com.bmos.lims2.common.enums.ScopeLimitTypeEnum;
import com.bmos.lims2.server.eln.record.util.BigDecimalUtil;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@ApiModel("数值判定公式配置")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumericalJudgmentConfig implements FormulaConfig{

    @ApiModelProperty("选项值")
    private String field;

    @ApiModelProperty("限制类型")
    private ScopeLimitTypeEnum limitType;

    @ApiModelProperty("等于值")
    private String numericalValue;

    @ApiModelProperty("限制范围")
    private Scope scope;


    /**
     * 数值判定公式:满足时录入值
     */
    @ApiModelProperty("数值判定公式:满足时录入值")
    private String satisfiedValue;

    /**
     * 数值判定公式:未满足时录入值
     * 注意 在多选或者单选组件上 未满足值为null
     */
    @ApiModelProperty("数值判定公式:未满足时录入值")
    private String unsatisfiedValue;

    @Data
    @ApiModel("限定范围")
    public class Scope {
        @ApiModelEnumProperty(value = "上限值比较", enumClass = ScopeCompareResultEnum.class)
        private Integer upperLimit;

        @ApiModelEnumProperty(value = "下限值比较", enumClass = ScopeCompareResultEnum.class)
        private Integer lowerLimit;

        @ApiModelProperty("最大值")
        private String scopeMax;

        @ApiModelProperty("最小值")
        private String scopeMin;
    }

    @Override
    public boolean configIsComplete() {
        if (StrUtil.isEmpty(satisfiedValue)) {
            return false;
        }
        if (Objects.equals(limitType, ScopeLimitTypeEnum.SCOPE_LIMIT)) {
            if (scope == null) {
                return false;
            }
            if (StrUtil.isEmpty(scope.scopeMax) && StrUtil.isEmpty(scope.scopeMin)) {
                return false;
            }
        } else {
            if (StrUtil.isEmpty(numericalValue)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String calculateResult(String input) {
        return judgeResult(input);
    }

    public String judgeResult(String value) {
        boolean satisfied;
        BigDecimal compareValue = BigDecimalUtil.toBigDecimal(value);
        if (Objects.equals(limitType, ScopeLimitTypeEnum.NUMBER_EQUALS)) {
            BigDecimal equalValue = new BigDecimal(this.numericalValue);
            satisfied = compareValue.compareTo(equalValue) == 0;
        } else {
            satisfied = (StrUtil.isEmpty(scope.scopeMax)
                    || ScopeCompareResultEnum.getResultListByUpperValue(scope.upperLimit).contains(compareValue.compareTo(new BigDecimal(scope.scopeMax))))
                    && (StrUtil.isEmpty(scope.scopeMin)
                    || ScopeCompareResultEnum.getResultListByLowerValue(scope.lowerLimit).contains(compareValue.compareTo(new BigDecimal(scope.scopeMin))));
        }
        return satisfied ? satisfiedValue : unsatisfiedValue;
    }

}