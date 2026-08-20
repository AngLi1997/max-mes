package com.bmos.mes.service.inspect.lims;

import com.bmos.mes.service.inspect.model.InspectInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/** 发起请验上下文（网关入参） */
@Getter
@Builder
public class InitiateInspectContext {
    /** 平台物料id（全系统统一标识，传给 LIMS） */
    private Long platformMaterialId;
    /** 请验单Id = LIMS 请验单配置id（作 templateId） */
    private Long inspectConfigId;
    /** 检验方案id */
    private Long schemeId;
    /** 检验方案版本id */
    private Long schemeVersionId;
    /** 物料批号 */
    private String materialBatchNo;
    /** 请验单信息（扁平字段，code→value 的来源） */
    private List<InspectInfo> inspectInfos;
}
