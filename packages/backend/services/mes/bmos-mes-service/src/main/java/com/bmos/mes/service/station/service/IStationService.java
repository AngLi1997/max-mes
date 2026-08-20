package com.bmos.mes.service.station.service;

import java.util.List;

/**
 * 工位相关service
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:37
 */
public interface IStationService {

    /**
     * 根据工序步骤id和组件id获取工位id集合
     * @param procedureStepModelId
     * @param componentId
     * @return
     */
    List<Long> getStationIdsByProcedureStepModelIdAndComponentId(Long procedureStepModelId, Long componentId, Long planId);


    /**
     * 根据组件实例id获取工位id集合
     * @param componentInstanceId 组件实例id
     * @return
     */
    List<Long> getStationIdListByComponentInstanceId(Long componentInstanceId);
}
