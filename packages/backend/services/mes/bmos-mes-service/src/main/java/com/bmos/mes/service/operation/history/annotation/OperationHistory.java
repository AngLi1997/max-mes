package com.bmos.mes.service.operation.history.annotation;


import com.bmos.mes.service.operation.history.aspect.OperationHistoryContext;
import com.bmos.mes.service.operation.history.enums.BusinessModule;
import com.bmos.mes.service.operation.history.enums.OperationType;

import java.lang.annotation.*;

/**
 * 操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationHistory {

    /**
     * 业务模块
     * @return 业务模块
     */
    BusinessModule module();

    /**
     * 操作类型
     * @return 操作类型
     */
    OperationType operationType();

    /**
     * 备注 取值表达式
     * 如: #id,#param.id
     * @return 表达式
     */
    String remark() default "";

    /**
     * 节点名称
     */
    String nodeName() default "";

    /**
     * 审批意见
     */
    String comment() default "";

    /**
     * 业务唯一键取值表达式
     *  1，当直接从接口参数中取值时，@#id,@param.id
     *  2，从上下文中取值，配合 {@link OperationHistoryContext}
     *     如：businessId="#test.id"
     *        OperationLogContext.putVariable("test.id","1") {@link OperationHistoryContext#putVariable(String, Object)}
     *     或 businessId="#getName"
     *        OperationLogContext.putVariable(user,User::getName) {@link OperationHistoryContext#putVariable(Object, OperationHistoryContext.VariableFunc)}
     * @return 表达式
     */
    String businessId();
}
