package com.bmos.lims2.web.recordprint.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 可打印检验单响应VO
 * @Author: yigaohui
 * @Date: 2025/11/25 10:25
 */
@Getter
@Setter
@ApiModel("可打印检验单响应VO")
public class PrintableInspectionOrderRespVO {

    @ApiModelProperty("检验单ID")
    private Long id;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("请验时间")
    private LocalDateTime inspectionRequestTime;
}


