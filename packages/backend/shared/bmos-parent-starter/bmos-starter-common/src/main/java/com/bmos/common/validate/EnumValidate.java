package com.bmos.common.validate;

import cn.hutool.core.util.ArrayUtil;
import com.bmos.common.base.enums.KeyValueEnum;
import com.bmos.common.base.enums.ValueEnum;
import com.bmos.common.util.enums.EnumUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 枚举值校验，即值只能是指定枚举类中的value值
 *
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = EnumValidate.EnumValidateValidator.class)
public @interface EnumValidate {

    /**
     * 枚举类(必须实现{@link ValueEnum}接口的枚举)
     */
    Class<? extends Enum<? extends ValueEnum>> value() default DefaultEnum.class;

    /**
     * 可选枚举值
     */
    String[] values() default {};

    /**
     * 字段名,如果字段名称为空，默认为枚举类去掉Enum标志且首字母小写（如：TestTypeEnum -> testType）
     */
    String name() default "";

    /**
     * 错误提示
     */
    String message() default "{name}枚举值错误，可选值为{enums}";

    /**
     * 用于分组校验
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class EnumValidateValidator implements ConstraintValidator<EnumValidate, Object> {

        private EnumValidate EnumValidate;

        @Override
        public void initialize(EnumValidate EnumValidate) {
            this.EnumValidate = EnumValidate;
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            if (value == null) {
                return true;
            }

            String[] values = EnumValidate.values();
            Class<? extends Enum<? extends ValueEnum>> enumClass = EnumValidate.value();
            Assert.isTrue(!ArrayUtil.isEmpty(values) || enumClass != DefaultEnum.class,
                    "@EnumValidate注解的values和value不能同时为默认值");

            // 如果没有限制枚举值，则直接强转判断即可
            if (ArrayUtil.isEmpty(values)) {
                if (EnumUtils.isExist((ValueEnum[]) EnumValidate.value().getEnumConstants(), value)) {
                    return true;
                }
                // 添加枚举值占位符值参数，校验失败的时候可用
                setErrorEnumPlaceholderValue(value, null, context);
                return false;
            }

            // 如果限制了可选枚举值，则使用可选枚举值判断
            // 如果value为Number或者Enum类型，则也将其统统转为字符串比较，方便些(Enum对象toString即为对象名)
            if (ArrayUtil.contains(values, value.toString())) {
                return true;
            }
            setErrorEnumPlaceholderValue(value, values, context);
            return false;
        }

        /**
         * 设置错误提示消息中的enum和name占位符值
         *
         * @param value   枚举字段值
         * @param values  可选值数组
         * @param context context
         */
        @SuppressWarnings({"all"})
        private void setErrorEnumPlaceholderValue(Object value, Object[] values, ConstraintValidatorContext context) {
            String message = EnumValidate.message();
            // message如果不包含name和enums占位符，则直接返回
            if (!message.contains("{name}") && !message.contains("{enums}")) {
                return;
            }
            // 值类型是否为枚举值类型，如为枚举值类型提示值需为枚举类实例对象名（即需调用枚举的toString方法）
            boolean valueTypeIsEnum = value instanceof KeyValueEnum && value instanceof Enum;
            // 如果值为枚举类型，则直接获取值对应的枚举值class即可
            Class<? extends Enum<? extends ValueEnum>> enumClass = valueTypeIsEnum ? (Class<? extends Enum<? extends ValueEnum>>) value.getClass() : EnumValidate.value();

            String enumsPlaceholderValue;
            if (enumClass == DefaultEnum.class) {
                enumsPlaceholderValue = Arrays.stream(values).map(Object::toString).collect(Collectors.joining(", "));
            } else {
                Enum<? extends ValueEnum>[] enums = enumClass.getEnumConstants();
                ValueEnum[] valueEnums = (ValueEnum[]) enums;
                // 如果是KeyValueEnum类型，则返回的错误信息带有name属性值
                if (KeyValueEnum.class.isAssignableFrom(enumClass)) {
                    // 如果限制可选值为空，则全量输出
                    if (ArrayUtil.isEmpty(values)) {
                        enumsPlaceholderValue = Arrays.stream((KeyValueEnum[]) enums).map(nv -> nv.getValue() + ":" + nv.getName())
                                .collect(Collectors.joining(", "));
                    } else {
                        enumsPlaceholderValue = Arrays.stream((KeyValueEnum[]) enums)
                                .filter(x -> ArrayUtil.contains(values, valueTypeIsEnum ? x.toString() : String.valueOf(x.getValue())))
                                .map(nv -> ((valueTypeIsEnum ? nv.toString() : nv.getValue()) + ":" + nv.getName()))
                                .collect(Collectors.joining(", "));
                    }
                } else {
                    if (ArrayUtil.isEmpty(values)) {
                        enumsPlaceholderValue = Arrays.stream(valueEnums).map(nv -> Objects.toString(nv.getValue()))
                                .collect(Collectors.joining(", "));
                    } else {
                        enumsPlaceholderValue = Arrays.stream(valueEnums)
                                .filter(x -> ArrayUtil.contains(values, x.getValue().toString()))
                                .map(nv -> Objects.toString(nv.getValue()))
                                .collect(Collectors.joining(", "));
                    }
                }
            }
            // 添加枚举值占位符值参数，校验失败的时候可用
            HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
            hibernateContext.addMessageParameter("enums", "[" + enumsPlaceholderValue + "]");
            // 如果字段名称为空，默认为枚举类去掉Enum标志且首字母小写（如：TestTypeEnum -> testType）
            if (StringUtils.isEmpty(EnumValidate.name()) && enumClass != DefaultEnum.class) {
                String defaultName = enumClass.getSimpleName();
                if (defaultName.endsWith("Enum")) {
                    defaultName = Character.toLowerCase(defaultName.charAt(0)) + defaultName.substring(1, defaultName.length() - 4);
                } else {
                    defaultName = Character.toLowerCase(defaultName.charAt(0)) + defaultName.substring(1);
                }
                hibernateContext.addMessageParameter("name", defaultName);
            }
        }
    }


    enum DefaultEnum implements ValueEnum<Integer> {
        ;

        @Override
        public Integer getValue() {
            return 0;
        }
    }

    enum DefaultKeyValueEnum implements KeyValueEnum<Integer> {
        ;

        @Override
        public Integer getValue() {
            return 0;
        }

        @Override
        public String getName() {
            return null;
        }
    }

}