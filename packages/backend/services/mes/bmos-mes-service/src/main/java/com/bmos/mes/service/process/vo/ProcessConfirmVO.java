package com.bmos.mes.service.process.vo;

import com.bmos.common.util.enums.EnumUtils;
import com.bmos.mes.common.enums.audit.FlowStateEnum;
import com.bmos.mes.common.enums.process.AuditPerorationStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@ApiModel("工艺审核vo")
public class ProcessConfirmVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    private String planBatchNo;

    @ApiModelProperty("生产开始时间")
    private String startTime;

    @ApiModelProperty("生产结束时间时间")
    private String endTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("审批结论")
    private AuditPerorationStateEnum confirmOpinion;
}
