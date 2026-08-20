package com.bmos.common.util.id;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

public class IdUtils {
    /**
     * 雪花算法id生成 IdUtil.getSnowflake指定参数可保证分布式系统中全局唯一
     *
     * @return Long
     */
    public static Long getSnowflake() {
        Snowflake snowflake = IdUtil.getSnowflake(1,1);
        return snowflake.nextId();
    }

    /**
     * 雪花算法id生成 IdUtil.getSnowflake指定参数可保证分布式系统中全局唯一
     *
     * @return Long
     */
    public static String getSnowflakeStr() {
        return String.valueOf(getSnowflake());
    }

    /**
     * 生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
     *
     * @return String
     */
    public static String getUUID() {
        return IdUtil.randomUUID();
    }

    /**
     * 生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
     *
     * @return String
     */
    public static String getSimpleUUID() {
        return IdUtil.simpleUUID();
    }
}

