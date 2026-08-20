package com.bmos.lims2.server.task.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 任务编号生成器
 * 
 * @author system
 * @since 2025/01/29
 */
public class TaskNoGenerator {

    private static final AtomicLong SEQUENCE = new AtomicLong(1);
    private static final String PREFIX = "TASK";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 生成任务编号
     * 格式：TASK + 日期(yyyyMMdd) + 序号(4位)
     * 例如：TASK202501290001
     */
    public static String generate() {
        String date = LocalDateTime.now().format(DATE_FORMAT);
        long seq = SEQUENCE.getAndIncrement();
        return String.format("%s%s%04d", PREFIX, date, seq % 10000);
    }

    /**
     * 重置序号（用于测试）
     */
    public static void resetSequence() {
        SEQUENCE.set(1);
    }
}
