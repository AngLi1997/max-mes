package com.bmos.lims2.server.eln.conclusion.service;

import com.bmos.lims2.server.eln.conclusion.dto.ConclusionComponentSaveDTO;

import java.util.List;

/**
 * @Description: 结论组件服务
 * @Author: yigaohui
 * @Date: 2025/11/19 10:25
 */
public interface ConclusionService {

    /**
     * 保存/更新结论组件值
     * @param dto
     */
    void saveOrUpdateConclusionComponent(ConclusionComponentSaveDTO dto);

    /**
     * 批量保存/更新结论组件值
     * @param dtoList
     */
    void batchSaveOrUpdateConclusionComponent(List<ConclusionComponentSaveDTO> dtoList);

    /**
     * 在自动判定生成时（LIMS侧计算出结论）为 APP-ELN 任务同步生成 ELN 结论组件值
     * @param taskId 任务ID
     * @param judgedResult 判定结果（true/false）
     */
    void syncAutoConclusionForEln(Long taskId, Boolean judgedResult);
} 

