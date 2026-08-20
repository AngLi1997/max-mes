package com.bmos.platform.common.constant;

import java.util.concurrent.ThreadPoolExecutor;

public interface ThreadPoolConstants {

    ThreadPoolExecutor MESSAGE_THREAD_POOL = new ThreadPoolExecutor(
            10,
            10,
            5,
            java.util.concurrent.TimeUnit.SECONDS,
            new java.util.concurrent.LinkedBlockingQueue<>(1000),
            new ThreadPoolExecutor.CallerRunsPolicy());

}
