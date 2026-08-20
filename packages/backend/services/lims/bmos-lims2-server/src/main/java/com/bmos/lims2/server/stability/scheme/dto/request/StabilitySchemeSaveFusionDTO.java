package com.bmos.lims2.server.stability.scheme.dto.request;

import lombok.Data;

import java.util.List;

/**
 * 稳定性方案全量暂存请求DTO（基础信息+分析项+检验计划，不校验必填）
 */
@Data
public class StabilitySchemeSaveFusionDTO {

    /** 基础信息 */
    private StabilitySchemeSaveDTO basic;

    /** 检验项目配置更新列表，对应 save-items（嵌套结构：检验项目→分析项列表） */
    private List<StabilitySchemeItemSaveDTO.ItemDTO> itemUpdates;

    /** 检验计划列表 */
    private StabilitySchemePlanSaveDTO planSave;
}
