package com.bmos.cache.redis.lock;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

@Aspect
@Order(Integer.MIN_VALUE)
public class DistributedLockAspect {

    private final Logger log = LoggerFactory.getLogger(DistributedLockAspect.class);

    //定义解析器
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();


    private static final LocalVariableTableParameterNameDiscoverer localVariableTable = new LocalVariableTableParameterNameDiscoverer();

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private final RedissonClient redissonClient;


    @Pointcut("@annotation(com.bmos.cache.redis.lock.DistributedLock)")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        //优先判断 key
        if (StrUtil.isNotEmpty(distributedLock.key())) {
            log.info("DistributedLock for key {}", distributedLock.key());
            return proceedByLock(joinPoint, distributedLock, distributedLock.key());
        }

        //再判断是否有表达式
        if (StrUtil.isNotEmpty(distributedLock.expression())) {
            StandardEvaluationContext evaluationContext = getEvaluationContext(joinPoint.getArgs(), method);
            Expression expression = PARSER.parseExpression(distributedLock.expression());
            log.info("DistributedLock for expression {}", distributedLock.expression());
            return proceedByLock(joinPoint, distributedLock, String.valueOf(expression.getValue(evaluationContext)));
        }

        //如果既没有key 也没有 expression,默认拼接 方法名和参数值作为key,md5 减短key的长度
        String methodArgs = method.getName() + StrUtil.join("", joinPoint.getArgs());
        String lockKey = SecureUtil.md5(methodArgs);
        log.info("DistributedLock for default  {}, {}", methodArgs, lockKey);
        return proceedByLock(joinPoint, distributedLock, lockKey);
    }

    private Object proceedByLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock, String lockKey) throws Throwable {
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(distributedLock.duration(), distributedLock.timeUnit());
            if (!locked) {
                throw new BmosException(BaseResponseCode.TRY_AGAIN_LATER);
            }
            log.info("lock success for key: {}", lockKey);
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("unlock success for key: {}", lockKey);
            }
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
