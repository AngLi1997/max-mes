package com.bmos.cache.redis.stabilization;

import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.web.ServletUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;

/**
 * @className: ApiStabilizationAspect
 * @author: yigaohui
 * @date: 2024/8/28 14:41
 * @Version: 1.0
 * @description: 接口防抖切面
 */
@Aspect
@Component
public class ApiStabilizationAspect {

    private final Logger log = LoggerFactory.getLogger(ApiStabilizationAspect.class);

    //定义解析器
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();


    private static final LocalVariableTableParameterNameDiscoverer localVariableTable = new LocalVariableTableParameterNameDiscoverer();

    public ApiStabilizationAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient; }

    private final RedissonClient redissonClient;


    @Pointcut("@annotation(com.bmos.cache.redis.stabilization.ApiStabilization)")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ApiStabilization apiStabilization = method.getAnnotation(ApiStabilization.class);
        RBucket<Object> bucket = redissonClient.getBucket(method.getDeclaringClass().getName() + "#" + method.getName());
        if (bucket.isExists()) {
            log.error("方法【{}】被【{}】重复调用:", bucket.getName(), bucket.get());
            throw new BmosException(BaseResponseCode.REPEAT_REQUEST);
        }
        String clientIP = ServletUtils.getClientIP();
        StandardEvaluationContext evaluationContext = getEvaluationContext(joinPoint.getArgs(), method);
        Expression expression = PARSER.parseExpression(apiStabilization.value());
        Object value = String.valueOf(expression.getValue(evaluationContext));
        Instant now = Instant.now();
        String cacheValue = now.toEpochMilli() + "#" + clientIP + "#" + value;
        log.info("设置方法【{}】防抖缓存【{}】", bucket.getName(), cacheValue);
        long interval = apiStabilization.interval();
        bucket.expire(now.plusSeconds(interval));
        boolean b = bucket.setIfAbsent(cacheValue);
        if (!b) {
            log.error("方法【{}】被【{}】重复调用:", bucket.getName(), bucket.get());
            throw new BmosException(BaseResponseCode.REPEAT_REQUEST);
        }
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            bucket.delete();
        }
    }


    /**
     * 填充spEL的评估上下文对象
     *
     * @param args   代理方法的请求参数
     * @param method 目标方法
     * @return 填充请求参数的上下文对象
     */
    private StandardEvaluationContext getEvaluationContext(Object[] args, Method method) {
        //获取参数名
        String[] parameterNames = localVariableTable.getParameterNames(method);
        StandardEvaluationContext context = new StandardEvaluationContext();
        //填充spEL的上下文对象
        if (parameterNames != null) {
            //获取参数信息
            //对象信息
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }
        }
        return context;
    }
}
