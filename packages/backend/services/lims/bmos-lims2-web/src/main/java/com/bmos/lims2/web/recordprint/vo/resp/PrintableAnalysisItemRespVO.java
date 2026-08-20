package com.bmos.lims2.web.recordprint.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 可打印分析项响应VO
 * @Author: yigaohui
 * @Date: 2025/11/25 10:35
 */
@Getter
@Setter
@ApiModel("可打印分析项响应VO")
public class PrintableAnalysisItemRespVO {

    @ApiModelProperty("分析项ID（lm_inspection_scheme_parameter.id）")
    private Long parameterId;

    @ApiModelProperty("分析项编码")
    private String parameterCode;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("任务ID（复核通过）")
    private Long taskId;

    @ApiModelProperty("分析项与记录绑定ID（lm_inspect_parameter_record.id）")
    private Long parameterRecordId;
}


