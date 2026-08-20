package com.bmos.web.swagger.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.common.validate.EnumValidate;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.core.annotation.AnnotationUtils;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.builders.PropertySpecificationBuilder;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.spi.service.ExpandedParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * swagger 枚举字段属性具体描述
 */

public class ApiModelEnumPropertyBuilder implements ModelPropertyBuilderPlugin, ExpandedParameterBuilderPlugin {

    @SuppressWarnings("all")
    @Override
    public void apply(ModelPropertyContext context) {
        if (context.getAnnotatedElement().isPresent()) {
            ApiModelProperty mp = AnnotationUtils.getAnnotation(context.getAnnotatedElement().get(), ApiModelProperty.class);
            if (mp != null) {
                return;
            }
        }
        BeanPropertyDefinition bpd = context.getBeanPropertyDefinition().orElse(null);
        if (bpd == null) {
            return;
        }
        ApiModelEnumProperty ev = bpd.getField().getAnnotation(ApiModelEnumProperty.class);
        if (ev == null) {
            return;
        }
        ModelPropertyBuilder builder = context.getBuilder();
        PropertySpecificationBuilder specificationBuilder = context.getSpecificationBuilder();
        builder.description(ev.value());
        builder.required(ev.required());

        // 获取字段类型
        Class<?> rawType = bpd.getField().getRawType();
        // 是否为枚举类型，为枚举类型，对应的value需为枚举值的名称。而不是调用getValue方法获取值，因为枚举类型前端需传枚举值对象名才可以转换为枚举对象
        boolean isEnumType = Enum.class.isAssignableFrom(rawType);

        AllowableListValues values = getAllowableListValues(rawType, ev);
        // 如果字段类型不为枚举类型且不为字符串，则将枚举值信息增加至描述信息中
//        if (!isEnumType && rawType != String.class) {
//            builder.description(ev.value() + "\n[" + String.join(",\n", values.getValues()) + "]");
//        } else {
//            builder.allowableValues(values);
//        }
        specificationBuilder.enumerationFacet(e -> e.allowedValues(values));
        specificationBuilder.description(ev.value() + "\n[" + String.join(",\n", values.getValues()) + "]");
        specificationBuilder.required(ev.required());
        specificationBuilder.allowEmptyValue(ev.required());
        specificationBuilder.example(getEnumExampleValue(values));
    }



    @Override
    public void apply(ParameterExpansionContext context) {
        ApiModelEnumProperty apiModelEnumProperty = context.findAnnotation(ApiModelEnumProperty.class).orElse(null);
        if (apiModelEnumProperty == null) {
            return;
        }
        AllowableListValues allowableListValues = getAllowableListValues(context.getFieldType().getErasedType(), apiModelEnumProperty);
        context.getParameterBuilder().required(apiModelEnumProperty.required())
                .description(apiModelEnumProperty.value() + " => [" + String.join(", ", allowableListValues.getValues()) + "]");
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    /**
     * 获取允许的枚举值
     *
     * @param fieldType            字段类型
     * @param apiModelEnumProperty 注解信息
     * @return
     */
    @SuppressWarnings("all")
    private AllowableListValues getAllowableListValues(Class<?> fieldType, ApiModelEnumProperty apiModelEnumProperty) {
        // 是否为枚举类型，为枚举类型，对应的value需为枚举值的名称。而不是调用getValue方法获取值，因为枚举类型前端需传枚举值对象名才可以转换为枚举对象
        boolean isEnumType = Enum.class.isAssignableFrom(fieldType);

        // enumClass为默认值，则获取字段对应的class
        Class<?> enumClass = apiModelEnumProperty.enumClass();
        if (enumClass == EnumValidate.DefaultEnum.class && isEnumType) {
            enumClass = fieldType;
        }
        KeyValueEnum[] enumConstants = (KeyValueEnum[]) enumClass.getEnumConstants();

        String[] vs = apiModelEnumProperty.values();
        // 如果values不为空，则为values中指定枚举值
        if (ArrayUtil.isEmpty(vs)) {
            return new AllowableListValues(Arrays.stream(enumConstants).map(x -> getPair(isEnumType ? x.toString() : String.valueOf(x.getValue()), x.getName()))
                    .collect(Collectors.toList()), enumClass.getName());
        }

        List<String> vl = new ArrayList<>();
        for (KeyValueEnum ce : enumConstants) {
            String code = isEnumType ? ce.toString() : String.valueOf(ce.getValue());
            if (ArrayUtil.contains(vs, code)) {
                vl.add(getPair(code, ce.getName()));
            }
        }
        return new AllowableListValues(vl, enumClass.getName());
    }

    private String getPair(String code, String name) {
        return code + ":" + name;
    }

    private String getEnumExampleValue(AllowableListValues values) {
        return CollUtil.getFirst(StrUtil.split(values.getValues().get(0), ":"));
    }
}
