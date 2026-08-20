package com.bmos.logging.aspect.defined;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

/**
 * @author renjinguang
 */
public class OperationUserDefinedContext {

    private static final Logger log = LoggerFactory.getLogger(OperationUserDefinedContext.class);

    private static final ThreadLocal<Stack<StandardEvaluationContext>> evaluationContextStack = new ThreadLocal<>();

    protected static void putEvaluationContext(StandardEvaluationContext context) {
        Stack<StandardEvaluationContext> contexts = evaluationContextStack.get();
        if (contexts == null) {
            contexts = new Stack<>();
        }
        contexts.push(context);
        evaluationContextStack.set(contexts);
    }

    public static void putVariable(String key, Object value) {
        Stack<StandardEvaluationContext> contexts = evaluationContextStack.get();
        if (contexts != null) {
            contexts.peek().setVariable(key, value);
        }
    }

    public static <T, R> void putVariable(T obj, VariableFunc<T, R> variableFunc) {
        Stack<StandardEvaluationContext> contexts = evaluationContextStack.get();
        if (contexts != null) {
            contexts.peek().setVariable(variableFunc.getMethodName(), variableFunc.getVar(obj));
        }
    }

    protected static void clear() {
        evaluationContextStack.get().pop();
        evaluationContextStack.remove();
    }


    @FunctionalInterface
    public interface VariableFunc<T, R> extends Serializable {

        R getVar(T t);

        default String getMethodName() {
            Method write;
            try {
                write = this.getClass().getDeclaredMethod("writeReplace");
                write.setAccessible(true);
                SerializedLambda invoke = (SerializedLambda) write.invoke(this);
                return invoke.getImplMethodName();
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                log.error("[OperationUserDefinedContext] 获取 [VariableFunc] 方法名错误：{}", e.getCause() + e.getMessage());
                throw new IllegalArgumentException("OperationUserDefinedContext lambda expression error");
            }
        }
    }
}
