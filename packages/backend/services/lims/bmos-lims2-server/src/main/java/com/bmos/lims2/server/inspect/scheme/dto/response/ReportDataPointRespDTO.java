package com.bmos.lims2.server.inspect.scheme.dto.response;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import lombok.Data;

/**
 * 报告数据点列表响应DTO
 */
@Data
public class ReportDataPointRespDTO {

    private Long schemeId;
    private Long versionId;
    private String versionNo;

    private Long inspectItemId;
    private String inspectItemCode;
    private String inspectItemName;

    private Long parameterId;
    private String parameterCode;
    private String parameterName;

    private Long dataPointId;
    private String dataPointName;
    private DataPointTypeEnum pointType;

    /**
     * 可作为模板占位符的唯一索引，避免使用易变的配置ID
     * 格式：${ITEM:${inspectItemCode}|PARAM:${parameterCode}|POINT:${dataPointId}}
     */
    private String indexPlaceholder;
}


