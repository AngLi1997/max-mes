package com.bmos.lims2.common.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @Description: 时长显示格式常量（替代枚举，直接使用格式值）
 * @Author: yigaohui
 * @Date: 2025/11/24 10:00
 */
public final class TimeDisplayFormats {

    private TimeDisplayFormats() {}

    public static final String DD_HH_MM_SS = "dd HH:mm:ss";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String MINUTE_SS = "mm:ss";
    public static final String SECOND = "ss";
    public static final String DD_HH_MM = "dd HH:mm";
    public static final String DD_HH = "dd HH";
    public static final String DD = "dd";
    public static final String HH_MM = "HH:mm";
    public static final String HH = "HH";
    public static final String MINUTE = "mm";

    public static final Set<String> ALLOWED = Collections.unmodifiableSet(new LinkedHashSet<>(
            Arrays.asList(
                    DD_HH_MM_SS, HH_MM_SS, MINUTE_SS, SECOND,
                    DD_HH_MM, DD_HH, DD, HH_MM, HH, MINUTE
            )
    ));
}



