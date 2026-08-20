package com.bmos.lims2.server.stability.scheme.service;

import com.bmos.lims2.server.stability.scheme.dto.request.StabilityCopyItemsResultDTO;
import com.bmos.lims2.server.stability.scheme.dto.request.StabilitySchemePlanSaveDTO;
import com.bmos.lims2.server.stability.scheme.dto.response.StabilitySchemePlanDTO;

import java.util.List;

/**
 * 稳定性方案检验计划Service接口
 */
public interface StabilitySchemePlanService {

    /**
     * 保存检验计划（全量覆盖）
     */
    void savePlans(StabilitySchemePlanSaveDTO saveDTO);

    /**
     * 查询版本下的检验计划列表
     */
    List<StabilitySchemePlanDTO> listPlans(Long versionId);

    /**
     * 删除检验计划（级联删除时间点）
     */
    void deletePlan(Long planId);

    /**
     * 删除时间点
     */
    void deleteTimepoint(Long timepointId);

    /**
     * 复制版本下的检验计划到新版本
     *
     * @param copyItemsResult copyItems 返回的 itemIdMap/paramIdMap，用于修正 TimepointParam 中的配置ID引用
     */
    void copyPlans(Long sourceVersionId, Long targetVersionId, Long schemeId, StabilityCopyItemsResultDTO copyItemsResult);

    /**
     * 校验版本下所有检验计划的时间点取样量合计不超过总取样量（含单位换算）
     * 若某计划下各时间点取样量之和（换算为计划单位后）大于总取样量，则抛出异常
     */
    void validatePlanTotalAmounts(Long versionId);
}
