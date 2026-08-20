package com.bmos.mes.service.config.executor;

import com.bmos.mes.service.config.async.AsyncTaskComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author yigaohui
 * @summary 线程类
 */
@Configuration
public class AsyncTaskConfig {

	@Bean
	public Executor asyncTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//配置核心线程数
		executor.setCorePoolSize(3);
		//配置最大线程数
		executor.setMaxPoolSize(30);
		//配置队列大小
		executor.setQueueCapacity(100);
		//配置线程池中的线程的名称前缀
		executor.setThreadNamePrefix("async-task-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		//执行初始化
		executor.initialize();
		return executor;
	}

	@Bean
	@DependsOn("asyncTaskExecutor")
	public AsyncTaskComponent asyncTaskComponent() {
		return new AsyncTaskComponent();
	}
}
