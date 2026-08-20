package com.bmos.mes.common.constant;

import java.util.concurrent.ThreadPoolExecutor;

public interface ThreadPoolConstants {

    ThreadPoolExecutor BATCH_THREAD_POOL = new ThreadPoolExecutor(
            100,
            200,
            60,
            java.util.concurrent.TimeUnit.SECONDS,
            new java.util.concurrent.LinkedBlockingQueue<>(1000),
            new ThreadPoolExecutor.CallerRunsPolicy());

}
