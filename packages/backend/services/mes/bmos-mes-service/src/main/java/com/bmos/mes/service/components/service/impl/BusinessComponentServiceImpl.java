package com.bmos.mes.service.components.service.impl;

import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.service.components.mapper.BusinessComponentInstanceMapper;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.components.service.IBusinessComponentService;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.mapper.ProcedureStepConfigMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.record.mapper.BatchRecordComponentMapper;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/12 09:39
 */
@Service
public class BusinessComponentServiceImpl implements IBusinessComponentService {

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private BatchRecordComponentMapper batchRecordComponentMapper;

    @Resource
    private BusinessComponentInstanceMapper businessComponentInstanceMapper;

    @Resource
    private ProcedureStepConfigMapper procedureStepConfigMapper;

    @Override
    public BusinessComponentInstance selectById(Long componentInstanceId) {
        return businessComponentInstanceMapper.selectById(componentInstanceId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BusinessComponentInstance getOrCreateComponentInstance(Long productPlanId, Long procedureStepModelId, Long componentId, Long copyVersion, Boolean reuse) {
        BusinessComponentInstance componentInstance = businessComponentInstanceMapper.getComponentInstance(productPlanId, procedureStepModelId, componentId, copyVersion, reuse);
        if (componentInstance != null){
            return componentInstance;
        }
        // 没有实例的话就初始化一个实例
        Plan plan = planMapper.selectById(productPlanId);
        if (plan == null){
            return null;
        }
        ProcedureStepModel procedureStepModel = procedureStepModelMapper.selectById(procedureStepModelId);
        if (procedureStepModel == null){
            return null;
        }
        BatchRecordComponent batchRecordComponent = batchRecordComponentMapper.selectById(componentId);
        if (batchRecordComponent == null){
            return null;
        }
        ProcedureStepConfig procedureStepConfig = procedureStepConfigMapper.selectComponentConfig(procedureStepModel.getId(), componentId, reuse, procedureStepModel.getProcessId(), procedureStepModel.getProcessVersion());
        componentInstance = new BusinessComponentInstance();
        componentInstance.setProductPlanId(productPlanId);
        componentInstance.setProcedureStepModelId(procedureStepModelId);
        componentInstance.setComponentId(componentId);
        componentInstance.setReuse(reuse);
        componentInstance.setCopyVersion(copyVersion);
        componentInstance.setProcedureStepId(procedureStepModel.getProcedureStepId());
        componentInstance.setProcessId(procedureStepModel.getProcessId());
        componentInstance.setProcessVersion(procedureStepModel.getProcessVersion());
        componentInstance.setRecordItemId(procedureStepModel.getRecordItemId());
        componentInstance.setRecordVersionId(procedureStepModel.getRecordVersionId());
        if (procedureStepConfig != null){
            componentInstance.setComponentConfigJson(procedureStepConfig.getConfigInfo());
            componentInstance.setProcedureStepConfigId(procedureStepConfig.getId());
        }
        componentInstance.setComponentType(BusinessComponentTypeEnum.getEnumByValue(batchRecordComponent.getComponentType()));
        componentInstance.setComponentName(batchRecordComponent.getComponentName());
        componentInstance.setBatchNo(plan.getBatchNo());
        businessComponentInstanceMapper.insert(componentInstance);
        return componentInstance;
    }
}
