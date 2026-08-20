package com.bmos.logging.aspect.defined;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.web.ServletUtils;
import com.bmos.logging.annotation.defined.OperationUserDefined;
import com.bmos.logging.aspect.ModelProcess;
import com.bmos.logging.enums.HeaderKeyEnum;
import com.bmos.logging.model.LogModel;
import com.bmos.logging.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @param <E> 各业务日志实体类 继承LogModel
 * @param <T> 各业务自身日志service 自己实现save
 */
@Aspect
@Slf4j
public abstract class OperationUserDefinedAspect<E extends LogModel, T extends OperationLogService<E>> implements ModelProcess<E> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private static final LocalVariableTableParameterNameDiscoverer localVariableTable = new LocalVariableTableParameterNameDiscoverer();

    public T service;

    public OperationUserDefinedAspect(T service) {
        this.service = service;
    }

    @Pointcut("@annotation(com.bmos.logging.annotation.defined.OperationUserDefined)")
    private void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //SPEL
        StandardEvaluationContext evaluationContext = getEvaluationContext(joinPoint.getArgs(), method);
        OperationUserDefinedContext.putEvaluationContext(evaluationContext);
        Object proceed;
        try{
            //方法上下文
            proceed = joinPoint.proceed(joinPoint.getArgs());
            E e = initLogModel();
            E logModel = setCommonProperties(e);
            OperationUserDefined annotation = method.getAnnotation(OperationUserDefined.class);
            Object remarkValue = evaluationExpr(evaluationContext, annotation.remark());
            logModel.setRemark(ObjectUtil.isEmpty(remarkValue) ? null : String.valueOf(remarkValue));
            Object operationObject = evaluationExpr(evaluationContext, annotation.operationObject());
            logModel.setOperationObject(ObjectUtil.isEmpty(operationObject) ? null : String.valueOf(operationObject));
            service.save(logModel);
            return proceed;
        } finally {
            OperationUserDefinedContext.clear();
        }
    }

    private E setCommonProperties(E logModel) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String menuIdStr = request.getHeader(HeaderKeyEnum.HEADER_KEY_MENU.getKey());
        String operationType = request.getHeader(HeaderKeyEnum.HEADER_KEY_OPERATION.getKey());
        String operationBusinessStr = request.getHeader(HeaderKeyEnum.HEADER_KEY_BUSINESS.getKey());
        String operationBusiness = URLDecoder.decode(operationBusinessStr, CharsetUtil.CHARSET_UTF_8);
        if (StrUtil.isNotBlank(menuIdStr)) {
            logModel.setMenuId(Long.valueOf(menuIdStr));
        }
        if (StrUtil.isNotBlank(operationType)) {
            logModel.setOperationType(Integer.valueOf(operationType));
        }
        logModel.setOperationBusiness(operationBusiness);
        logModel.setIp(ServletUtils.getClientIP());
        SysUser user = SysUserHolder.getUser();
        logModel.setUserName(user.getUserName());
        logModel.setLoginName(user.getLoginName());
        logModel.setUserId(user.getUserId());
        return logModel;

    }

    private Object evaluationExpr(StandardEvaluationContext evaluationContext, String expression) {
        if (expression.startsWith("#")) {
            Expression expr = PARSER.parseExpression(expression);
            return expr.getValue(evaluationContext);
        }
        return null;
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
