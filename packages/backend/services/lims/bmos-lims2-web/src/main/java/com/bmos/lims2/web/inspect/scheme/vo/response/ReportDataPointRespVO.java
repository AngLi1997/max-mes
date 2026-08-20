package com.bmos.lims2.web.inspect.scheme.vo.response;

import com.bmos.lims2.common.enums.DataPointTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("报告数据点分页响应")
public class ReportDataPointRespVO {

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

    @ApiModelProperty("占位符索引")
    private String indexPlaceholder;
}


