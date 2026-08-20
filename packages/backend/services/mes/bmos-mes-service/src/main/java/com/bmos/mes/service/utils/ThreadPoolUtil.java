package com.bmos.mes.service.utils;

import java.util.concurrent.*;

/**
 * 异步线程池工具类
 *
 * @author liang
 * @version 1.0.0
 * @date 2023/11/11 00:35
 */
public class ThreadPoolUtil {

    private static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(10, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    public static void execute(Runnable runnable) {
        EXECUTOR_SERVICE.execute(runnable);
    }

    public static <T> Future<T> submit(Callable<T> callable) {
        return EXECUTOR_SERVICE.submit(callable);
    }

    public static void shutdown() {
        EXECUTOR_SERVICE.shutdown();
    }
}
