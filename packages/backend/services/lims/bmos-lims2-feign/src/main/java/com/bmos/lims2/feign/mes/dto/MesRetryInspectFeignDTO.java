package com.bmos.lims2.feign.mes.dto;

import lombok.Data;
import java.util.Map;

@Data
public class MesRetryInspectFeignDTO {
    /** 原检验单号（LIMS orderNo），用于作废原单 */
    private String originOrderNo;
    /** 平台物料id（全系统统一标识） */
    private Long platformMaterialId;
    private Long inspectConfigId;
    private Long schemeId;
    private Long schemeVersionId;
    private String materialBatchNo;
    private Map<String, String> fields;
    private Map<String, String> fieldNames;
    private Map<String, Boolean> fieldRequired;
    /** 来源系统：MES / WMS（默认按 MES 兼容旧调用方） */
    private String sourceSystem;
}
