package com.bmos.common.util.date;

import cn.hutool.core.util.BooleanUtil;
import lombok.experimental.UtilityClass;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

/**
 * @Author yigaohui
 * @Description 日期转换帮助类
 * @Date 2023/7/21 10:26
 */
@UtilityClass
public class DateUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATE_HOURS_PATTERN = "yyyy-MM-dd HH:mm";

    /**
     * 时间类型-毫秒数定义
     */
    public static final long MS_1SECOND = 1000;
    public static final long MS_1MINUTE = 60 * MS_1SECOND;
    public static final long MS_1HOUR = 60 * MS_1MINUTE;
    public static final long MS_1DAY = 24 * MS_1HOUR;

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 时间字符串格式：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime toLocalDateTime(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        return LocalDateTime.parse(dateTime, ISO_DATE_TIME);
    }

    public static LocalDateTime toLocalDateTime(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static LocalDate toLocalDate(Long timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
    }

    public static Long toMill(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 计算两个日期之间的间隔天数
     *
     * @param startTime
     * @param endTime
     * @param includeWeekend 是否包含周末
     * @return
     */

    public static List<String> rangeDateList(Date startTime, Date endTime, Boolean includeWeekend) throws ParseException {
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return Collections.emptyList();
        }
        //日期工具类准备
        DateFormat format = new SimpleDateFormat(DATE_PATTERN);
        String dBegin = format.format(startTime);

        //设置开始时间
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(format.parse(dBegin));

        // 两个时间相差的天数
        int days = (int) ((endTime.getTime() - startTime.getTime()) / MS_1DAY);

        //装返回的日期集合容器
        List<String> rangeDateList = new ArrayList<>(days);
        // 每次循环给calBegin日期加一天,包含endTime
        for (int i = 0; i <= days; i++) {
            Date time = calBegin.getTime();
            if (BooleanUtil.isFalse(includeWeekend)) {
                if (!isWeekend(time)) {
                    rangeDateList.add(format.format(time));
                }
            } else {
                rangeDateList.add(format.format(time));
            }
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
        }
        return rangeDateList;
    }

    /**
     * 日期是否是周末
     *
     * @param date
     * @return
     */
    public static Boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

}
