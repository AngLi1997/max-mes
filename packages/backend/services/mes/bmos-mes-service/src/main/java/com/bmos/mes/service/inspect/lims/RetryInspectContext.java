package com.bmos.mes.service.inspect.lims;

import com.bmos.mes.service.inspect.model.InspectInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/** 重新发起上下文（方案B：作废原单+新建） */
@Getter
@Builder
public class RetryInspectContext {
    /** 原检验单号（= 原 LIMS orderNo），用于作废原单 */
    private String originInspectNo;
    /** 平台物料id（全系统统一标识，传给 LIMS） */
    private Long platformMaterialId;
    private Long inspectConfigId;
    private Long schemeId;
    private Long schemeVersionId;
    private String materialBatchNo;
    private List<InspectInfo> inspectInfos;
}
