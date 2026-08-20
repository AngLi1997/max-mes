package com.bmos.mes.service.process.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bmos.mes.common.enums.process.task.ExpressionTypeEnum;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.dto.task.CheckoutExpressionDTO;
import com.bmos.mes.service.process.dto.task.ExpressionSaveDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.task.ProcedureCondition;
import com.bmos.mes.service.process.model.task.ProcedureExpression;
import com.bmos.mes.service.process.vo.Task.ExpressionDetailVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProcedureExpressionService extends IService<ProcedureExpression> {


    void insertBatch(List<ProcedureExpression> expressions);

    Map<String, List<ExpressionDetailVO>> getListByProcedureModelId(Long procedureModelId);


    void deleteByIds(List<ExpressionSaveDTO> dto);

    Map<Long, List<ProcedureExpression>> getMapByProcedureStepModeIds(Set<Long> stepModelId);

    Boolean checkoutExpression(CheckoutExpressionDTO dto);

    List<ProcedureExpression> getMapByStepOrTask(List<Long> convertList);

    List<ProcedureExpression> getExpressionListByStepModelIds(List<Long> taskIds);

    void save(List<ProcedureStepDTO> execute, ProcedureModel procedureModel);
    /**
     * 通过工步模型id删除表达式
     * @param modelIds 工步模型id
     */
    void deleteByProcedureStepModelIds(List<Long> modelIds);

    /**
     * 通过工序步骤模型id获取表达式
     * @param stepModeIds 工序步骤模型id
     */
    List<ProcedureExpression> getByProcedureStepModelIds(List<Long> stepModeIds);

    void saveProcedureExpression(List<ProcedureModel> procedureModels);

    void updateExpressionAndCondition(List<ProcedureModel> procedureModels);

    void deleteByProcedureModelIds(List<Long> modelIdList);

    List<ExpressionDetailVO> selectByModelId(List<Long> convertList,String nodeType);

    List<String> getConfigByModelId(List<Long> procedureModelId);

    List<String> getStepModelCondition(List<Long> stepModelId,List<String> conditionType);

    List<ProcedureExpression> startPlanConditionList(Set<Long> stepModelIds,Set<Long> ids);

    List<ProcedureCondition> selectMaterialConditionListByStepModelId(List<Long> convertList, String type);

}
