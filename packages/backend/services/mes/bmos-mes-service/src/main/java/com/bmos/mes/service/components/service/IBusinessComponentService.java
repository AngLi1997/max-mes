package com.bmos.mes.service.components.service;

import com.bmos.mes.service.components.model.BusinessComponentInstance;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/12 09:39
 */
public interface IBusinessComponentService {

    BusinessComponentInstance selectById(Long componentInstanceId);

    BusinessComponentInstance getOrCreateComponentInstance(Long productPlanId, Long procedureStepModelId, Long componentId, Long copyVersion, Boolean reuse);
}
