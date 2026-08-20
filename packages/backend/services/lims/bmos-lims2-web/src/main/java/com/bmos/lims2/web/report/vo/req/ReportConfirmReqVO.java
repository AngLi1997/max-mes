package com.bmos.lims2.web.report.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报告确认请求VO
 */
@Data
@ApiModel("报告确认请求")
public class ReportConfirmReqVO {

    @ApiModelProperty("检验结论")
    private String inspectionConclusion;

    @ApiModelProperty("备注")
    private String confirmRemark;
}
