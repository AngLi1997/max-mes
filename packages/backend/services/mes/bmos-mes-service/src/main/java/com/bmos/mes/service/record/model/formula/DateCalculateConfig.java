package com.bmos.mes.service.record.model.formula;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.utils.TimeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

@Data
@ApiModel("日期计算公式配置")
@Slf4j
public class DateCalculateConfig implements FormulaConfig{

    @ApiModelProperty("日期格式")
    private String datePattern;

    @ApiModelProperty("时间单位")

    private String timeUnit;

    @ApiModelProperty("日期样式")
    private String dateStyle;

    @ApiModelProperty("时间差")
    private Integer timeDiff;

    @ApiModelProperty("计算方式(是否增加时间)")
    private Boolean addTime;

    @Override
    public boolean configIsComplete() {
        if (StrUtil.isEmpty(datePattern) || timeDiff == null
                || StrUtil.isEmpty(timeUnit) || addTime == null) {
            return false;
        }
        return true;
    }

    /**
     * @param input 输入值 对于日期计算公式 需要传入拓展json
     * @return
     */
    @Override
    public String calculateResult(String input) {
        LocalDateTime beforeTime = getExtLocalDateTime(input);
        if (beforeTime == null) {
            log.error("获取拓展值时间失败,executeFormData.extInfo:{}", input);
            return null;
        }
        LocalDateTime plus = beforeTime.plus(addTime ? timeDiff : -timeDiff, getChronoUnitByValue(timeUnit));
        return LocalDateTimeUtil.format(plus, datePattern);
    }

    public Long calculateTimeStamp(String input) {
        LocalDateTime localDateTime = getExtLocalDateTime(input);
        return TimeUtil.getTimestamp(localDateTime);
    }

    private static ChronoUnit getChronoUnitByValue(String value) {
        return Arrays.stream(ChronoUnit.values())
                .filter(e -> StrUtil.equalsIgnoreCase(e.name(), value))
                .findFirst()
                .orElse(null);
    }

}
