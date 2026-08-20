package com.bmos.mes.common.utils;

public class CompactSnowflake {
    private final static long EPOCH = 1735689600L; // 2023-01-01 00:00:00 UTC
    private final static int MACHINE_BITS = 3;     // 支持8台机器
    private final static int SEQUENCE_BITS = 7;    // 每台机器每秒128个ID

    private final static int MAX_MACHINE_ID = (1 << MACHINE_BITS) - 1;
    private final static int MAX_SEQUENCE = (1 << SEQUENCE_BITS) - 1;

    private final int machineId;
    private long lastTimestamp = -1L;
    private int sequence = 0;

    public CompactSnowflake(int machineId) {
        if (machineId > MAX_MACHINE_ID) {
            throw new IllegalArgumentException("机器ID超过最大值");
        }
        this.machineId = machineId;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨异常");
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                timestamp = tilNextSecond(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;
        return ((timestamp - EPOCH) << (MACHINE_BITS + SEQUENCE_BITS))
                | (machineId << SEQUENCE_BITS)
                | sequence;
    }

    private long tilNextSecond(long lastTimestamp) {
        long timestamp;
        do {
            timestamp = timeGen();
        } while (timestamp <= lastTimestamp);
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis() / 1000; // 秒级时间
    }

    public static void main(String[] args) {
        CompactSnowflake generator = new CompactSnowflake(5); // 机器ID=5
        System.out.println(generator.nextId()); // 输出类似 5123456789
    }
}
