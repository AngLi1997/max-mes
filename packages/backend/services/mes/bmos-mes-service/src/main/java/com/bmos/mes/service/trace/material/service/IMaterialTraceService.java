package com.bmos.mes.service.trace.material.service;

import com.bmos.mes.service.trace.material.vo.MaterialTraceVO;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 09:55
 */
public interface IMaterialTraceService {

    MaterialTraceVO traceData(Long productPlanId);
}
