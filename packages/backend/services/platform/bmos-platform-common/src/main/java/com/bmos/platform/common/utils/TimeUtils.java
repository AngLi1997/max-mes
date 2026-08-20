package com.bmos.platform.common.utils;

import com.bmos.platform.common.GlobalConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class TimeUtils {

    public static long convertStringToTimeStamp(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(GlobalConstants.DATE_TIME_FORMAT);
        Date date = sdf.parse(dateStr);
        return date.getTime();
    }

    public static LocalDateTime endOfYear(LocalDateTime dateTime){
        return LocalDateTime.of(dateTime.getYear(), 12, 31, 23, 59, 59);
    }

    public static LocalDateTime beginOfYear(LocalDateTime upperEndpoint) {
        return LocalDateTime.of(upperEndpoint.getYear(), 1, 1, 0, 0, 0);
    }

    public static LocalDateTime convertTimeStampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }
    public static LocalDateTime convertTimeStampToLocalDateTime(Long validTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(validTime), ZoneId.systemDefault());
    }
}
