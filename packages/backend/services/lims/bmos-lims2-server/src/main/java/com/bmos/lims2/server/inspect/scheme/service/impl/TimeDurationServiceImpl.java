package com.bmos.lims2.server.inspect.scheme.service.impl;

import com.bmos.lims2.server.eln.record.dto.DateCalculateVO;
import com.bmos.lims2.server.inspect.scheme.service.TimeDurationService;
import com.bmos.lims2.common.enums.RoundingRuleEnum;
import com.bmos.lims2.common.constants.TimeDisplayFormats;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @Description: 时长计算服务实现（独立实现，避免外部依赖）
 * @Author: yigaohui
 * @Date: 2025/10/31 10:00
 */
@Service
public class TimeDurationServiceImpl implements TimeDurationService {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public DateCalculateVO preview(String startTime, String endTime, String calculateType, RoundingRuleEnum roundingUp) {
        DateCalculateVO vo = new DateCalculateVO();
        if (startTime == null || endTime == null) {
            vo.setCalculateResult("0秒");
            vo.setTimeSeconds("0");
            return vo;
        }
        long diffSeconds;
        try {
            long s = SDF.parse(startTime).getTime();
            long e = SDF.parse(endTime).getTime();
            diffSeconds = Math.max(0, (e - s) / 1000);
        } catch (ParseException e) {
            vo.setCalculateResult("0秒");
            vo.setTimeSeconds("0");
            return vo;
        }

        // 基础拆分
        long days = diffSeconds / 86400;
        long hours = (diffSeconds % 86400) / 3600;
        long minutes = (diffSeconds % 3600) / 60;
        long seconds = diffSeconds % 60;

        boolean up = RoundingRuleEnum.UP.equals(roundingUp);
        String typeValue = (calculateType == null || !TimeDisplayFormats.ALLOWED.contains(calculateType))
                ? TimeDisplayFormats.DD_HH_MM_SS : calculateType;

        String result;
        long finalSeconds;

        switch (typeValue) {
            case TimeDisplayFormats.DD_HH_MM_SS: {
                // 最小单位：秒；向上则秒保持不变（无更小单位）
                result = build(days, hours, minutes, seconds);
                finalSeconds = days * 86400 + hours * 3600 + minutes * 60 + seconds;
                break;
            }
            case TimeDisplayFormats.HH_MM_SS: {
                long totalHours = days * 24 + hours;
                // 最小单位：秒；无更小单位
                result = build(0, totalHours, minutes, seconds);
                finalSeconds = totalHours * 3600 + minutes * 60 + seconds;
                break;
            }
            case TimeDisplayFormats.MINUTE_SS: {
                long totalMinutes = days * 24 * 60 + hours * 60 + minutes;
                // 最小单位：秒；无更小单位
                result = build(0, 0, totalMinutes, seconds);
                finalSeconds = totalMinutes * 60 + seconds;
                break;
            }
            case TimeDisplayFormats.SECOND: {
                long total = diffSeconds; // 最小单位秒，按舍入规则：有更小单位无
                result = total + "秒";
                finalSeconds = total;
                break;
            }
            case TimeDisplayFormats.DD_HH_MM: {
                // 最小单位：分；若存在秒且向上，则分+1
                if (up && seconds > 0) {
                    minutes += 1;
                    if (minutes >= 60) { minutes = 0; hours += 1; }
                    if (hours >= 24) { hours = 0; days += 1; }
                }
                result = build(days, hours, minutes, 0);
                finalSeconds = days * 86400 + hours * 3600 + minutes * 60;
                break;
            }
            case TimeDisplayFormats.DD_HH: {
                // 最小单位：时；若存在分或秒且向上，则时+1
                if (up && (minutes > 0 || seconds > 0)) {
                    hours += 1;
                    if (hours >= 24) { hours = 0; days += 1; }
                }
                result = build(days, hours, 0, 0);
                finalSeconds = days * 86400 + hours * 3600;
                break;
            }
            case TimeDisplayFormats.DD: {
                // 最小单位：日；若存在时分秒且向上，则日+1
                if (up && (hours > 0 || minutes > 0 || seconds > 0)) {
                    days += 1;
                }
                result = (days) + "日";
                finalSeconds = days * 86400;
                break;
            }
            case TimeDisplayFormats.HH_MM: {
                long totalHours = days * 24 + hours;
                // 最小单位：分；若存在秒且向上，则分+1
                if (up && seconds > 0) {
                    minutes += 1;
                    if (minutes >= 60) { minutes = 0; totalHours += 1; }
                }
                result = build(0, totalHours, minutes, 0);
                finalSeconds = totalHours * 3600 + minutes * 60;
                break;
            }
            case TimeDisplayFormats.HH: {
                long totalHours = days * 24 + hours;
                if (up && (minutes > 0 || seconds > 0)) {
                    totalHours += 1;
                }
                result = totalHours + "时";
                finalSeconds = totalHours * 3600;
                break;
            }
            case TimeDisplayFormats.MINUTE: {
                long totalMinutes = days * 24 * 60 + hours * 60 + minutes;
                if (up && seconds > 0) {
                    totalMinutes += 1;
                }
                result = totalMinutes + "分";
                finalSeconds = totalMinutes * 60;
                break;
            }
            default: {
                // 默认按 dd HH:mm:ss
                result = build(days, hours, minutes, seconds);
                finalSeconds = days * 86400 + hours * 3600 + minutes * 60 + seconds;
            }
        }

        vo.setCalculateResult(result);
        vo.setTimeSeconds(String.valueOf(finalSeconds));
        return vo;
    }

    private String build(long days, long hours, long minutes, long seconds) {
        StringBuilder sb = new StringBuilder();
        if (days > 0) { sb.append(days).append("日"); }
        if (hours > 0) { sb.append(hours).append("时"); }
        if (minutes > 0) { sb.append(minutes).append("分"); }
        if (seconds > 0) { sb.append(seconds).append("秒"); }
        if (sb.length() == 0) { return "0秒"; }
        return sb.toString();
    }
}


