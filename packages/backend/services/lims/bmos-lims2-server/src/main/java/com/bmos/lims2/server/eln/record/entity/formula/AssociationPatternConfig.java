package com.bmos.lims2.server.eln.record.entity.formula;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.expression.enums.RoundingEnum;
import com.bmos.lims2.common.enums.NumberPatternEnum;
import com.bmos.lims2.common.model.execute.ExecuteFormDataMultiTimeExtInfo;
import com.bmos.lims2.server.eln.record.component.BusinessComponentStrategy;
import com.bmos.lims2.server.eln.record.util.BigDecimalUtil;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@ApiModel("关联引用公式格式配置")
@Data
public class AssociationPatternConfig implements FormulaConfig {

    @ApiModelProperty("数值格式")
    private NumberPatternConfig numberPatternConfig;

    @ApiModelProperty("日期格式")
    private DatePatternConfig datePatternConfig;

    @Override
    public boolean configIsComplete() {
        if (numberPatternConfig == null && datePatternConfig == null) {
            return false;
        }
        if (numberPatternConfig != null) {
            return numberPatternConfig.configIsComplete();
        }
        return datePatternConfig.configIsComplete();
    }

    public boolean isNumberType() {
        return numberPatternConfig != null;
    }

    /**
     * @param input 输入值 对于日期格式配置 需要传入拓展值
     * @return
     */
    @Override
    public String calculateResult(String input) {
        if (numberPatternConfig != null) {
            return numberPatternConfig.calculateResult(input);
        }
        return datePatternConfig.calculateResult(input);
    }

    @Data
    private static class NumberPatternConfig implements FormulaConfig {

        @ApiModelEnumProperty(enumClass = NumberPatternEnum.class, value = "数值样式")
        private Integer style;

        @ApiModelProperty("精度")
        private Integer scale;

        @ApiModelProperty("修约规则")
        private String roundCode;

        @ApiModelProperty("预输入")
        private String preInput;

        @ApiModelProperty("预览")
        private String preview;

        @ApiModelProperty("指数小写")
        private Boolean exponentLower;


        @Override
        public boolean configIsComplete() {
            if (style == null) {
                return false;
            }
            NumberPatternEnum value = NumberPatternEnum.getEnumByValue(style);
            boolean scaleCompleted = (scale != null && StrUtil.isNotEmpty(roundCode))
                    || (scale == null && StrUtil.isEmpty(roundCode));
            switch (value) {
                case REVISION_NUMBER:
                    if (scale == null || StrUtil.isEmpty(roundCode)) {
                        return false;
                    }
                    return true;
                case PERCENTAGE:
                    return scaleCompleted;
                case SCIENTIFIC_NOTATION:
                    if (exponentLower == null || !(scaleCompleted)) {
                        return false;
                    }
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public String calculateResult(String input) {
            BigDecimal number = BigDecimalUtil.isValidNumber(input) ? BigDecimalUtil.toBigDecimal(input) : new BigDecimal(input);
            NumberPatternEnum pattern = NumberPatternEnum.getEnumByValue(style);
            RoundingMode roundingMode = RoundingEnum.getEnumByCode(roundCode).getMapping();
            switch (pattern) {
                case REVISION_NUMBER:
                    return number.setScale(scale, roundingMode).toPlainString();
                case PERCENTAGE:
                    return BigDecimalUtil.toPercentageStr(number, scale, roundingMode);
                case SCIENTIFIC_NOTATION:
                    String result;
                    if (scale == null) {
                        result = BigDecimalUtil.toScientific(number);
                    } else {
                        result = BigDecimalUtil.toScientific(number, scale, roundingMode);
                    }
                    return BooleanUtil.isTrue(exponentLower) ? result.toLowerCase() : result.toUpperCase();
                default:
                    return null;
            }
        }
    }

    @Data
    private static class DatePatternConfig implements FormulaConfig {

        @ApiModelProperty("日期样式")
        private String dateStyle;

        @ApiModelProperty("日期格式")
        private String datePattern;

        @Override
        public boolean configIsComplete() {
            return StrUtil.isNotEmpty(dateStyle);
        }

        @Override
        public String calculateResult(String input) {
            ExecuteFormDataMultiTimeExtInfo extInfo = JsonUtils.parseObject(input, ExecuteFormDataMultiTimeExtInfo.class);
            if (extInfo == null) {
                return null;
            }
            if (BooleanUtil.isTrue(extInfo.getMultiLine())) {
                return BusinessComponentStrategy.getLFStrings(extInfo.getTimestampList().stream().map(e -> {
                    if (e == null) {
                        return StrUtil.DASHED;
                    }
                    return LocalDateTimeUtil.format(LocalDateTimeUtil.of(e, ZoneId.systemDefault()), dateStyle);
                }).collect(Collectors.toList()));
            }
            LocalDateTime extLocalDateTime = getExtLocalDateTime(input);
            return LocalDateTimeUtil.format(extLocalDateTime, dateStyle);
        }
    }
}
