package com.bmos.lims2.feign.mes.dto;

import lombok.Data;
import java.util.Map;

@Data
public class MesInitiateInspectFeignDTO {
    /** 平台物料id（全系统统一标识） */
    private Long platformMaterialId;
    /** 请验单Id（作 LIMS templateId） */
    private Long inspectConfigId;
    private Long schemeId;
    private Long schemeVersionId;
    private String materialBatchNo;
    /** 扁平字段：InspectConfigDataCodeEnum.code → value */
    private Map<String, String> fields;
    /** 字段展示名：code → showName */
    private Map<String, String> fieldNames;
    /** 字段必填：code → required */
    private Map<String, Boolean> fieldRequired;
    /** 来源系统：MES / WMS（默认按 MES 兼容旧调用方） */
    private String sourceSystem;
}
