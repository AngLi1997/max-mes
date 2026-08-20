package com.bmos.mes.common.utils;

import cn.hutool.core.util.StrUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/2 13:35
 */
public class TimeUtil {

    // 常用的日期格式
    public static final String F_DATE = "yyyy-MM-dd";
    // 常用的时间格式
    public static final String F_DATETIME = "yyyy-MM-dd HH:mm:ss";

    public static final String F_DATETIME_MINUTE = "yyyy-MM-dd HH:mm";

    /**
     * 获取时间戳
     * @param time 时间
     * @return 时间戳（秒级）
     */
    public static Long getTimestamp(LocalDateTime time) {
        if (time == null){
            return null;
        }
        // 结合系统默认时区转为ZonedDateTime
        ZonedDateTime zonedDateTime = time.atZone(ZoneId.systemDefault());
        // 将ZonedDateTime转换为时间戳
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 获取日期末位时间戳
     * @param date 日期
     * @return yyyy-MM-dd 23:59:59 的时间戳（秒级）
     */
    public static Long getEndOfDateTimestamp(LocalDate date) {
        if (date == null){
            return null;
        }
        LocalDateTime localDateTime = date.atStartOfDay().plusDays(1).minusSeconds(1);
        return getTimestamp(localDateTime);
    }

    /**
     * 获取日期末位时间戳
     * @param date 日期
     * @return yyyy-MM-dd 00:00:00 的时间戳（秒级）
     */
    public static Long getStartOfDateTimestamp(LocalDate date) {
        if (date == null){
            return null;
        }
        LocalDateTime localDateTime = date.atStartOfDay();
        return getTimestamp(localDateTime);
    }

    /**
     * 判断是否为日期格式
     * @param dateStr 日期字符串
     * @param formats 日期格式 不传默认使用 F_DATE, F_DATETIME
     * @return true/false
     */
    public static boolean isDateFormat(String dateStr, String... formats){
        if (StrUtil.isBlank(dateStr)){
            return false;
        }
        if (formats == null || formats.length == 0){
            formats = new String[]{ F_DATE, F_DATETIME };
        }
        for (String formatStr : formats) {
            if (formatStr.equals(F_DATE)){
                try {
                    LocalDate.parse(dateStr);
                    return true;
                }catch (Exception ignored){

                }
            }else if (formatStr.equals(F_DATETIME)){
                try {
                    LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(F_DATETIME));
                    return true;
                }catch (Exception ignored){

                }
            }
        }
        return false;
    }

    /**
     * 将字符串日期转换为时间戳
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static long convertStringToTimeStamp(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(F_DATETIME);
        Date date = sdf.parse(dateStr);
        return date.getTime();
    }

    public static long convertDateTimeStringToTimeStamp(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(dateStr);
        return date.getTime();
    }


    /**
     * @param timeString 将日时分秒字符串转换为秒值
     * @return
     */
    public static Integer convertToSeconds(String timeString) {
        try {
            int days = 0, hours = 0, minutes = 0, seconds = 0;

            // 使用正则表达式匹配时间单位
            Pattern pattern = Pattern.compile("(-?\\d+)日|(-?\\d+)时|(-?\\d+)分|(-?\\d+)秒");
            Matcher matcher = pattern.matcher(timeString);

            while (matcher.find()) {
                if (matcher.group(1) != null) {
                    days = Integer.parseInt(matcher.group(1));
                } else if (matcher.group(2) != null) {
                    hours = Integer.parseInt(matcher.group(2));
                } else if (matcher.group(3) != null) {
                    minutes = Integer.parseInt(matcher.group(3));
                } else if (matcher.group(4) != null) {
                    seconds = Integer.parseInt(matcher.group(4));
                }
            }

            // 将所有单位转换为秒数
            int totalSeconds = days * 86400 + hours * 3600 + minutes * 60 + seconds;
            return totalSeconds;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将秒值转换为日时分秒字符串
     * @param totalSeconds
     * @return
     */
    public static String convertSecondsToString(int totalSeconds) {
        if (totalSeconds == 0) {
            return "0秒";
        }
        int days = totalSeconds / 86400;
        int hours = (totalSeconds % 86400) / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        StringBuilder timeString = new StringBuilder();
        if (days > 0) {
            timeString.append(days).append("日");
        }
        if (hours > 0) {
            timeString.append(hours).append("时");
        }
        if (minutes > 0) {
            timeString.append(minutes).append("分");
        }
        if (seconds > 0) {
            timeString.append(seconds).append("秒");
        }
        return timeString.toString();
    }

    public static LocalDateTime endOfYear(LocalDateTime dateTime){
        return LocalDateTime.of(dateTime.getYear(), 12, 31, 23, 59, 59);
    }

    public static LocalDateTime beginOfYear(LocalDateTime upperEndpoint) {
        return LocalDateTime.of(upperEndpoint.getYear(), 1, 1, 0, 0, 0);
    }
}
