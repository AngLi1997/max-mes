package com.bmos.mybatis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.bmos.common.util.id.IdUtils;

/**
 * ID生成器，修改mybatisplus自定义的
 * @see IdType ASSIGN_ID 雪花算法生成方式
 */
public class CustomIdGenerator implements IdentifierGenerator {
    @Override
    public Long nextId(Object entity) {
        return IdUtils.getSnowflake();
    }

    public static Long nextId(){
        return IdUtils.getSnowflake();
    }
}
