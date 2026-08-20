package com.bmos.mes.common.enums.execute;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.util.i18n.I18nUtils;
import com.bmos.mes.common.model.execute.ExceptionDescriptionParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 修订异常枚举
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ModifyExceptionEnum implements CommonEnum<String> {

    MODIFY("MODIFY", "修订", "数据修订，将{}修订为{}，复核人{}", param -> {
        String template = I18nUtils.getCodeMessage("ModifyExceptionEnum.MODIFY.DESCRIPTION", "数据修订，将'{}'修订为'{}'，复核人'{}'", null).replaceAll("'\\{}'", "{}");
        return StrUtil.format(template, param.getOriginalValue(), param.getValue(), param.getReviewerName());
    }),
    SAVE("SAVE", "保存", "数据录入,{}", param -> {
        String template = I18nUtils.getCodeMessage("ModifyExceptionEnum.SAVE.DESCRIPTION", "数据录入,'{}'", null).replaceAll("'\\{}'", "{}");
        return StrUtil.format(template, param.getValue());
    }),
    TAKE_PICTURE("TAKE_PICTURE", "拍照上传", "取证人:{},取证时间:{}", param -> {
        String template = I18nUtils.getCodeMessage("ModifyExceptionEnum.TAKE_PICTURE.DESCRIPTION", "取证人:'{}',取证时间:'{}'", null).replaceAll("'\\{}'", "{}");
        return StrUtil.format(template, param.getUserName(),
                DateUtil.format(param.getOperationTime(), DatePattern.NORM_DATETIME_PATTERN));
    }),
    ;

    @EnumValue
    private final String value;

    private final String name;

    private final String description;

    private final Function<ExceptionDescriptionParam, String> buildDescription;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static ModifyExceptionEnum getEnumByValue(String value) {
        return Arrays.stream(ModifyExceptionEnum.values())
                .filter(statusEnum -> statusEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }

}
