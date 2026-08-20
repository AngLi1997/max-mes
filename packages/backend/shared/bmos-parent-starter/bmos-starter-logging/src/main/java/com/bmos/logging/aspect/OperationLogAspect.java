package com.bmos.logging.aspect;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.user.SysUser;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.common.util.web.ServletUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.logging.enums.HeaderKeyEnum;
import com.bmos.logging.model.LogModel;
import com.bmos.logging.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <E> 各业务日志实体类 继承LogModel
 * @param <T> 各业务自身日志service 自己实现save
 */
@Aspect
@Slf4j
public abstract class OperationLogAspect<E extends LogModel, T extends OperationLogService<E>> implements ModelProcess<E> {
    public T service;

    public OperationLogAspect(T service) {
        this.service = service;
    }

    @Pointcut("@annotation(com.bmos.logging.annotation.OperationLog)")
    private void pointCut() {
    }

    @AfterReturning("pointCut()")
    public void logAfter(JoinPoint joinPoint) {
        E e = initLogModel();
        E logModel = setCommonProperties(e, joinPoint);
        try {
            service.save(logModel);
        } catch (Exception ex) {
            log.error("操作日志保存失败:" + ex.getCause() + ex.getMessage());
        }
    }

    private E setCommonProperties(E logModel, JoinPoint joinPoint) {
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
        List<Object> args = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> !(arg instanceof HttpServletResponse)
                        && !(arg instanceof HttpServletRequest)
                        && !(arg instanceof MultipartFile))
                .collect(Collectors.toList());
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        OperationLog annotation = method.getAnnotation(OperationLog.class);
        // 过滤掉不需要记录的字段赋值操作对象
        filterNoNeededFields(args, annotation);
        String operationObject = JsonUtils.toJsonString(args);
        logModel.setOperationObject(operationObject);
        // 备注
        logModel.setRemark(getRemark(args, annotation));
        return logModel;
    }

    /**
     * 过滤掉参数中不需要保存的字段
     *
     * @param args
     */
    private static void filterNoNeededFields(List<Object> args, OperationLog annotation) {
        String type = annotation.filterArgType();
        String[] fields = annotation.filterFields();
        if (StrUtil.isNotBlank(type)) {
            Class<?> filterClass = null;
            try {
                filterClass = Class.forName(type);
                for (Object arg : args) {
                    if (filterClass.isInstance(arg)) {
                        for (String field : fields) {
                            Field declaredField = arg.getClass().getDeclaredField(field);
                            declaredField.setAccessible(Boolean.TRUE);
                            declaredField.set(arg, null);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                log.error("Class not found:" + type);
            } catch (NoSuchFieldException e) {
                log.error("field not found:" + e.getMessage());
            } catch (IllegalAccessException e) {
                log.error("IllegalAccessException:" + e.getMessage());
            }
        }
    }

    private String getRemark(List<Object> args, OperationLog annotation) {
        String remarkMethod = annotation.remark();
        try {
            for (Object arg : args) {
                Method argGetRemark = arg.getClass().getMethod(remarkMethod);
                Object invoke = argGetRemark.invoke(arg);
                if (ObjectUtil.isNotNull(invoke)) {
                    return invoke.toString();
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
        return null;
    }
}
