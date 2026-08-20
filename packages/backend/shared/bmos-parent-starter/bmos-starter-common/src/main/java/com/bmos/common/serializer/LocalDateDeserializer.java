package com.bmos.common.serializer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    public static final LocalDateDeserializer INSTANCE = new LocalDateDeserializer();

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (StrUtil.isBlank(p.getValueAsString())) {
            return null;
        }
        return LocalDateTimeUtil.parseDate(p.getValueAsString(), DatePattern.NORM_DATE_FORMATTER);
    }
}
