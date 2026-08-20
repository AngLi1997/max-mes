package com.bmos.mes.service.operation.history.aspect;


import com.bmos.mes.service.operation.history.annotation.OperationHistory;
import com.bmos.mes.service.operation.history.model.OperationLogModel;
import com.bmos.mes.service.operation.history.service.OperationHistoryService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class OperationHistoryAspect {

    private final Logger log = LoggerFactory.getLogger(OperationHistoryAspect.class);

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer localVariableTable = new LocalVariableTableParameterNameDiscoverer();

    @Autowired
    private OperationHistoryService operationHistoryService;

    @Pointcut("@annotation(com.bmos.mes.service.operation.history.annotation.OperationHistory)")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //SPEL
        StandardEvaluationContext evaluationContext = getEvaluationContext(joinPoint.getArgs(), method);
        OperationHistoryContext.putEvaluationContext(evaluationContext);
        Object proceed;
        OperationLogModel operationLogModel;
        try {
            //方法上下文
            proceed = joinPoint.proceed(joinPoint.getArgs());
            OperationHistory operationHistory = method.getAnnotation(OperationHistory.class);
            operationLogModel = buildOperationLogModel(evaluationContext, operationHistory);
            operationHistoryService.save(operationLogModel);
            log.info("操作日志保存成功：{}", operationLogModel);
            return proceed;
        } finally {
            // 清除上下文
            OperationHistoryContext.clear();
        }
    }

    private OperationLogModel buildOperationLogModel(StandardEvaluationContext evaluationContext, OperationHistory operationHistory) {
        Object remarkValue = evaluationExpr(evaluationContext, operationHistory.remark());
        Object nodeNameValue = evaluationExpr(evaluationContext, operationHistory.nodeName());
        Object commentValue = evaluationExpr(evaluationContext, operationHistory.comment());
        String remark = remarkValue == null ? null : String.valueOf(remarkValue);
        String nodeName = nodeNameValue == null ? null : String.valueOf(nodeNameValue);
        String comment = commentValue == null ? null : String.valueOf(commentValue);
        return OperationLogModel.builder()
                .module(operationHistory.module().name())
                .operationType(operationHistory.operationType().getValue())
                .remark(remark)
                .comment(comment)
                .nodeName(nodeName)
                .businessId((Long) evaluationExpr(evaluationContext, operationHistory.businessId()))
                .build();
    }

    private Object evaluationExpr(StandardEvaluationContext evaluationContext, String expression) {
        if (validateExpression(expression)) {
            Expression expr = PARSER.parseExpression(expression);
            return expr.getValue(evaluationContext);
        }
        return null;
    }

    private static boolean validateExpression(String expression) {
        return expression.startsWith("#");
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
