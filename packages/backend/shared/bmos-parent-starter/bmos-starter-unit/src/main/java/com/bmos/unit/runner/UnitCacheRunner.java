package com.bmos.unit.runner;

import com.bmos.unit.service.UnitCache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * UnitCacheRunner
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/27 00:44
 */
public class UnitCacheRunner implements ApplicationRunner {

    private final UnitCache unitCache;

    public UnitCacheRunner(UnitCache unitCache) {
        this.unitCache = unitCache;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        unitCache.reload();
    }
}
