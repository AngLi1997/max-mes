package com.bmos.mes.service.process.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mes.service.process.model.task.ProcedureCondition;

import java.util.List;

public interface ProcedureConditionService extends IService<ProcedureCondition> {


    void insertBatch(List<ProcedureCondition> conditions);

    List<ProcedureCondition> selectListByExpressionIdList(List<Long> expressionIdList);


    void deleteByExpressionIdlList(List<Long> expressIdList);

    /**
     * 通过工步模型id删除条件
     *
     * @param modelIds 工步模型id
     */
    void deleteByProcedureStepModelIds(List<Long> modelIds);

    /**
     * 通过步骤模型id获取条件
     *
     * @param modeIds 步骤模型id
     * @return 获取结果
     */
    List<ProcedureCondition> getByProcedureStepModelIds(List<Long> modeIds);

    void deleteByIds(List<Long> conditionIds);

    List<String> getStepModelCondition(List<Long> stepModelId,List<String> conditionType);

    List<ProcedureCondition> selectMaterialConditionListByStepModelId(List<Long> convertList, String type);
}
