package com.bmos.common.base.enums;

import com.bmos.common.util.i18n.I18nUtils;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 15:09
 */
public class CommonEnumSerializer extends JsonSerializer<KeyValueEnum> {

    public static final CommonEnumSerializer INSTANCE = new CommonEnumSerializer();

    Logger log = LoggerFactory.getLogger(CommonEnumSerializer.class);

    @Override
    public void serialize(KeyValueEnum keyValueEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 对所有枚举进行国际化
        String name = I18nUtils.getEnumMessage(keyValueEnum);
        try{
            jsonGenerator.writeObject(new CommonEnumVO<>(keyValueEnum.getValue(), name, name));
        }catch (JsonGenerationException e){
            jsonGenerator.writeObject(keyValueEnum);
        }

    }
}
