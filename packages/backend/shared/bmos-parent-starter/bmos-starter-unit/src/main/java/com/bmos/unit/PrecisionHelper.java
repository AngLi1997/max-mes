package com.bmos.unit;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.unit.exception.PrecisionAnnotationException;
import com.bmos.unit.service.UnitCache;
import com.bmos.unit.vo.CacheUnit;
import com.bmos.unit.vo.UnitRate;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bmos.common.exception.BaseResponseCode.UNIT_NOTFOUND;

/**
 * 修约工具
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/29 18:17
 */
public class PrecisionHelper {

    private static final Logger log = LoggerFactory.getLogger(PrecisionHelper.class);

    /**
     * 修约单位基数（能被10整除的）
     */
    private static final String[] UNIT = {"1", "2", "5"};

    /**
     * 解析精度
     *
     * @param unionPrecisionStr 精度解析字符串
     * @return 修约规则
     */
    private static Pre getPre(String unionPrecisionStr) {
        String s = unionPrecisionStr
                .replaceAll("0", "")
                .replaceAll("\\.", "");
        // 校验精度格式
        if (!ArrayUtil.contains(UNIT, s)) {
            throw new RuntimeException("修约精度不合法");
        }
        BigDecimal unit = new BigDecimal(s)
                .divide(BigDecimal.TEN, 1, RoundingMode.HALF_UP);
        BigDecimal unionPrecision = new BigDecimal(unionPrecisionStr);
        return Pre.builder()
                .unit(unit)
                .precision(unionPrecision.divide(unit, RoundingMode.HALF_UP).stripTrailingZeros())
                .fillZero(unionPrecision.scale())
                .build();
    }

    /**
     * 根据修约精度修约
     *
     * @param value             拟修约数值
     * @param unionPrecisionStr 修约精度
     *                          eg. 0.020 = 0.2 * 0.1
     *                          其中，0.2 代表以0.2单位进行修约，0.1 代表 修约后小数位保留1位，0.020的小数三位代表修约后不满三位填充0
     * @param roundingMode      舍入模式
     * @return
     */
    public static String precision(String value, String unionPrecisionStr, RoundingMode roundingMode) {
        return precision(new BigDecimal(value), unionPrecisionStr, roundingMode).toPlainString();
    }

    /**
     * 根据修约精度修约
     *
     * @param value             拟修约数值
     * @param unionPrecisionStr 修约精度
     *                          eg. 0.020 = 0.2 * 0.1
     *                          其中，0.2 代表以0.2单位进行修约，0.1 代表 修约后小数位保留1位，0.020的小数三位代表修约后不满三位填充0
     * @param roundingMode      舍入模式
     * @return
     */
    public static BigDecimal precision(BigDecimal value, String unionPrecisionStr, RoundingMode roundingMode) {
        Pre pre = getPre(unionPrecisionStr);
        String format = "%." + pre.fillZero + "f";
        log.info("====================");
        log.info("修约单位:{}", pre.unit);
        log.info("修约有效数字:{}", pre.precision.toPlainString());
        log.info("修约补零:{}", pre.fillZero + "位");
        log.info("====================");
        BigDecimal plus = value.divide(pre.unit, roundingMode);
        BigDecimal plusAfterPrecision = plus.setScale(pre.precision.scale(), roundingMode);
        String result = String.format(format, plusAfterPrecision.multiply(pre.unit));
        log.info("拟修约数值X\t\t{}", value.toPlainString());
        log.info("X/{}\t\t\t\t{}", pre.unit, plus.toPlainString());
        log.info("X/{}修约值\t\t\t{}", pre.unit, plusAfterPrecision.toPlainString());
        log.info("修约值\t\t\t{}", result);
        log.info("====================");
        return new BigDecimal(result);
    }

    /**
     * 根据单位信息修约
     *
     * @param value  拟修约数值
     * @param unitId 单位id
     * @return 修约结果
     */
    public static String precision(String value, Long unitId) {
        return precision(new BigDecimal(value), unitId).toPlainString();
    }

    /**
     * 根据单位信息修约
     *
     * @param value  拟修约数值
     * @param unitId 单位id
     * @return 修约结果
     */
    public static BigDecimal precision(BigDecimal value, Long unitId) {
        UnitCache unitCache = SpringUtil.getBean(UnitCache.class);
        CacheUnit unit = unitCache.getGlobalUnit(unitId);
        if (unit == null) {
            throw new BmosException(UNIT_NOTFOUND);
        }
        return value.setScale(unit.getPrecision().intValue(), unit.getRounding().getMapping());
    }

    /**
     * 列表单位转换
     *
     * @param list         数据列表（包含修约注解）
     */
    public static void convertUnitRenderList(List<?> list) {
        UnitCache unitCache = SpringUtil.getBean(UnitCache.class);
        try {
            if (CollectionUtil.isEmpty(list)) {
                return;
            }
            // 分组 -> 值 -> 单位转换率
            Map<String, UnitRate> rateMap = new HashMap<>();
            Object first = list.get(0);
            Class<?> clazz = first.getClass();
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(PrecisionUnitId.class)) {
                    if (declaredField.getType() != Long.class && declaredField.getType() != Long.TYPE) {
                        throw new PrecisionAnnotationException("@PrecisionUnitId只能出现在Long类型上");
                    }
                    putGroup(rateMap, declaredField.getAnnotation(PrecisionUnitId.class).group(), declaredField.getName(), PrecisionFieldType.UNIT_ID);
                }
                if (declaredField.isAnnotationPresent(PrecisionValue.class)) {
                    if (declaredField.getType() != BigDecimal.class && declaredField.getType() != String.class) {
                        throw new PrecisionAnnotationException("@PrecisionValue只能出现在BigDecimal或String类型上");
                    }
                    putGroup(rateMap, declaredField.getAnnotation(PrecisionValue.class).group(), declaredField.getName(), PrecisionFieldType.VALUE);
                }
            }

            for (Object o : list) {
                for (UnitRate rate : rateMap.values()) {
                    if (CollectionUtil.isEmpty(rate.getValueFields()) || StrUtil.isBlank(rate.getUnitIdField())) {
                        continue;
                    }
                    CacheUnit unit = getUnit(rate, o);
                    if (unit == null) {
                        continue;
                    }
                    // 渲染值
                    Set<String> valueFields = rate.getValueFields();
                    for (String valueField : valueFields) {
                        Field field = clazz.getDeclaredField(valueField);
                        field.setAccessible(true);
                        Method method = clazz.getMethod("get" + StringUtils.capitalize(valueField));
                        Object oldValueObject = method.invoke(o);
                        BigDecimal oldValue;
                        boolean isString;
                        if (oldValueObject instanceof String) {
                            oldValue = new BigDecimal((String) oldValueObject);
                            isString = true;
                        } else if (oldValueObject instanceof BigDecimal) {
                            oldValue = (BigDecimal) oldValueObject;
                            isString = false;
                        } else {
                            throw new PrecisionAnnotationException("@PrecisionValue只能出现在BigDecimal或String类型上");
                        }
                        BigDecimal finalValue = unitCache.toExt(oldValue, unit.getUnitId());
                        // 根据目标单位修约
                        String precision = precision(finalValue.toPlainString(), unit.getUnitId());
                        field.set(o, isString ? precision : new BigDecimal(precision));
                    }

                }
            }
        } catch (Exception e) {
            log.error("单位转换失败:{}", e.getMessage());
        }
    }

    /**
     * 统计字段 单位转换比例、转换单位id
     *
     * @param rateMap   统计map
     * @param group     分组
     * @param fieldName 字段名
     * @param type      注解类型
     */
    private static void putGroup(Map<String, UnitRate> rateMap, String group, String fieldName, PrecisionFieldType type) {
        if (!rateMap.containsKey(group)) {
            UnitRate unitRate = new UnitRate();
            rateMap.put(group, unitRate);
        }
        switch (type) {
            case UNIT_ID:
                rateMap.get(group).setUnitIdField(fieldName);
                break;
            case VALUE:
                rateMap.get(group).getValueFields().add(fieldName);
                break;
            default:
                break;
        }
    }

    /**
     * 根据单位id或者比例获取单位
     *
     * @param unitRate
     * @param obj
     * @return
     */
    private static CacheUnit getUnit(UnitRate unitRate, Object obj) {
        if (unitRate == null) {
            return null;
        }
        try {
            if (StrUtil.isNotBlank(unitRate.getUnitIdField())) {
                // 单位id转比例
                Field declaredField = obj.getClass().getDeclaredField(unitRate.getUnitIdField());
                declaredField.setAccessible(true);
                Method method = obj.getClass().getMethod("get" + StringUtils.capitalize(unitRate.getUnitIdField()));
                Object o = method.invoke(obj);
                long unitId = Long.parseLong(o.toString());
                return SpringUtil.getBean(UnitCache.class).getGlobalUnit(unitId);
            } else {
                // 没有转换比例
                return null;
            }
        } catch (Exception e) {
            log.error("获取单位信息失败:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 格式化BigDecimal
     *
     * @param value 数值
     * @return 字符串
     */
    public static String formatBigDecimal(BigDecimal value) {
        if (value == null) {
            return "0";
        }
        return value.stripTrailingZeros().toPlainString();
    }

    /**
     * 修约规则
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    private static final class Pre {

        /**
         * 修约单位
         */
        private BigDecimal unit;

        /**
         * 有效数字
         */
        private BigDecimal precision;

        /**
         * 补零小数位
         */
        private Integer fillZero;
    }
}
