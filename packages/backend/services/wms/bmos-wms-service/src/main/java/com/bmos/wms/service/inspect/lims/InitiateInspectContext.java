package com.bmos.wms.service.inspect.lims;

import com.bmos.wms.service.inspect.model.InspectInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 发起请验上下文（网关入参）。
 *
 * <p>WMS 默认 sourceSystem = WMS（在 BmosLimsGateway 内显式写入 feign DTO）。
 */
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
    /** 货品批号 */
    private String materialBatchNo;
    /** 请验单字段（扁平 code → value） */
    private List<InspectInfo> inspectInfos;
}
