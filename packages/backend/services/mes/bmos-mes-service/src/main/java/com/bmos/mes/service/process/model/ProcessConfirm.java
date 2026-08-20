package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.process.AuditPerorationStateEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@TableName("bm_process_confirm")
public class ProcessConfirm extends BaseDO {

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;

    @ApiModelProperty("产品规格")
    private String productSpecification;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("生产批号")
    private String planBatchNo;

    @ApiModelProperty("生产开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("生产结束时间时间")
    private LocalDateTime endTime;

    @ApiModelProperty("填报结论时间")
    private LocalDateTime confirmTime;

    @ApiModelProperty("审批结论")
    private AuditPerorationStateEnum confirmOpinion;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("流程id")
    private String instanceId;
}
