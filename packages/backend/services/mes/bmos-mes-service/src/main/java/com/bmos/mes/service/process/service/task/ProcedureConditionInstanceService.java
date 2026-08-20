package com.bmos.mes.service.process.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.model.task.ProcedureConditionInstance;
import com.bmos.mes.service.process.model.task.ProcedureExpression;

import java.util.List;

public interface ProcedureConditionInstanceService extends IService<ProcedureConditionInstance> {

    List<ProcedureConditionInstance> queryByExpressionIds(List<Long> convertList, Long plan);

    void deleteByIds(List<Long> ids);

    List<ProcedureConditionInstance> selectByExpressionIds(List<Long> convertList,Long planId);

    /**
     * 查找任务没有完成的条件
     *
     * @param conditionType 条件类型
     * @param id
     * @return 查询结果
     */
    List<ProcedureConditionInstance> selectConditionList(String conditionType, Long id);

    /**
     * 根据条件类型和计划id查询
     *
     * @param planId         计划id
     * @param stepModeId     步骤模型id
     * @param expressionType 表达式类型
     * @return 查询结果
     */
    List<ProcedureConditionInstance> selectByPlanAndExpressionType(Long planId, Long stepModeId,
                                                                   String expressionType);


    /**
     * 初始化条件实例
     * @param needUpdatePlan 需要更新的计划
     */
    void initConditionInstance(Plan needUpdatePlan);

    List<ProcedureConditionInstance> selectByPlanIdAndStepModelIds(List<Long> taskId, Long planId);

    /**
     * 获取所有进行中计划的条件
     * @param planId
     * @return
     */
    List<ProcedureExpression> startPlanConditionList(List<Long> planId);
}
