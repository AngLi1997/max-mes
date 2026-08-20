package com.bmos.mes.service.config.async;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @Author yigaohui
 * @Description 异步任务分片
 */
@Slf4j
public class AsyncTaskComponent {

    @Resource
    private Executor asyncTaskExecutor;

    /**
     * @return java.util.List<R>
     * @Author yigaohui
     * @Description 任务分片，大任务切割-一个入参
     * @Date 2023/9/12 14:49
     * @Param [totalList, partitionStrategy, function]
     */
    public <T, R> List<R> listPartition(List<T> totalList, Integer partitionStrategy,
                                        Function<List<T>, List<R>> function) {
        if (CollectionUtil.isNotEmpty(totalList)) {
            List<List<T>> partition = Lists.partition(totalList, partitionStrategy);
            List<CompletableFuture<List<R>>> asyncList = new ArrayList<>(partition.size());

            partition.forEach(t -> {
                // 开启异步任务
                CompletableFuture<List<R>> async = CompletableFuture.supplyAsync(() -> function.apply(t),
                        asyncTaskExecutor);
                asyncList.add(async);
            });

            try {
                // 等待所有任务执行完
                CompletableFuture.allOf(asyncList.toArray(new CompletableFuture[1])).get();
                List<R> result = new ArrayList<>(partition.size());
                // 遍历查询结果
                for (CompletableFuture<List<R>> future : asyncList) {
                    result.addAll(future.get());
                }
                return result;
            } catch (Exception ex) {
                log.error("async task error", ex);
            }
        }

        return new ArrayList<>();
    }

    /**
     * @return java.util.List<R>
     * @Author yigaohui
     * @Description 任务分片，大任务切割-两个入参
     * @Date 2023/9/12 14:49
     * @Param [totalList, partitionStrategy, u, function]
     */
    public <T, U, R> List<R> listPartition(List<T> totalList, Integer partitionStrategy, U u, BiFunction<List<T>, U,
            List<R>> function) throws ExecutionException, InterruptedException {
        if (CollectionUtil.isNotEmpty(totalList)) {
            List<List<T>> partition = Lists.partition(totalList, partitionStrategy);
            List<CompletableFuture<List<R>>> asyncList = new ArrayList<>(partition.size());

            partition.forEach(t -> {
                // 开启异步任务
                CompletableFuture<List<R>> async = CompletableFuture.supplyAsync(() -> function.apply(t, u),
                        asyncTaskExecutor);
                asyncList.add(async);
            });

            // 等待所有任务执行完
            CompletableFuture.allOf(asyncList.toArray(new CompletableFuture[1])).get();
            List<R> result = new ArrayList<>(partition.size());
            // 遍历查询结果
            for (CompletableFuture<List<R>> future : asyncList) {
                result.addAll(future.get());
            }
            return result;
        }

        return new ArrayList<>();
    }

    /**
     * @return java.util.List<R>
     * @Author yigaohui
     * @Description 任务合并
     * @Date 2023/9/12 14:50
     * @Param [t, functions]
     */
    @SafeVarargs
    public final <T, R> List<R> addTask(T t, Function<T, R>... functions) {
        if (ArrayUtil.isEmpty(functions)) {
            return new ArrayList<>();
        }
        try {
            List<CompletableFuture<R>> asyncList = new ArrayList<>(functions.length);
            for (Function<T, R> function : functions) {
                CompletableFuture<R> completableFuture = CompletableFuture.supplyAsync(() -> function.apply(t),
                        asyncTaskExecutor);
                asyncList.add(completableFuture);
            }
            CompletableFuture.allOf(asyncList.toArray(new CompletableFuture[1])).get();
            List<R> result = new ArrayList<>(functions.length);
            // 遍历查询结果
            for (CompletableFuture<R> future : asyncList) {
                result.add(future.get());
            }
            return result;
        } catch (Exception ex) {
            log.error("async task error", ex);
        }
        return new ArrayList<>();
    }
}
