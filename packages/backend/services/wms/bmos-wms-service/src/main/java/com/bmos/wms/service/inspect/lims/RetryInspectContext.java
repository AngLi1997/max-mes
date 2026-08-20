package com.bmos.wms.service.inspect.lims;

import com.bmos.wms.service.inspect.model.InspectInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * 重新发起请验上下文（方案 B：作废原单 + 新建）。
 *
 * <p>WMS 默认 sourceSystem = WMS（在 BmosLimsGateway 内显式写入 feign DTO）。
 */
@Getter
@Builder
public class RetryInspectContext {

    /** 原 LIMS 检验单号 */
    private String originInspectNo;
    /** 平台物料id */
    private Long platformMaterialId;
    private Long inspectConfigId;
    private Long schemeId;
    private Long schemeVersionId;
    private String materialBatchNo;
    private List<InspectInfo> inspectInfos;
}
