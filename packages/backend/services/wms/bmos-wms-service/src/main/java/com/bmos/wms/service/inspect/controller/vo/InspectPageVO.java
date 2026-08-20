package com.bmos.wms.service.inspect.controller.vo;

import com.bmos.wms.common.enums.inspect.InspectStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("WMS 检验单分页VO")
public class InspectPageVO {

    @ApiModelProperty("检验单id")
    private Long id;

    @ApiModelProperty("LIMS 检验单号")
    private String inspectNo;

    @ApiModelProperty("请验状态")
    private InspectStatusEnum status;

    @ApiModelProperty("退回原因 / 重发起原因")
    private String reason;

    @ApiModelProperty("货品id")
    private Long cargoId;

    @ApiModelProperty("货品名称")
    private String cargoName;

    @ApiModelProperty("货品合并编码")
    private String mergeCode;

    @ApiModelProperty("货品批号")
    private String materialBatchNo;

    @ApiModelProperty("请验人")
    private String inspector;

    @ApiModelProperty("请验时间")
    private LocalDateTime inspectTime;
}
