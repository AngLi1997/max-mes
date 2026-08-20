package com.bmos.lims2.web.stability.sample.vo.response;

import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性样品行响应VO
 */
@Data
@ApiModel("稳定性样品行")
public class StabilitySampleRowRespVO {

    @ApiModelProperty("lm_stability_plan_sample.id")
    private Long id;

    @ApiModelProperty("批次ID")
    private Long batchId;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("lm_sample.id")
    private Long sampleId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("接收数量")
    private String actualSampleAmount;

    @ApiModelProperty("当前样品数量")
    private String currentQuantity;

    @ApiModelProperty("取样量单位")
    private String sampleUnit;

    @ApiModelProperty("取样量单位名称")
    private String sampleUnitName;

    @ApiModelProperty("接收日期")
    private LocalDate receiveDate;

    @ApiModelProperty("接收人")
    private String receiverName;

    @ApiModelProperty("试验类型")
    private String experimentType;

    @ApiModelProperty("试验类型名称")
    private String experimentTypeName;

    @ApiModelProperty("试验储存条件")
    private String storageCondition;

    @ApiModelProperty("储存位置")
    private String storageLocation;

    @ApiModelProperty("样品状态")
    private StabilityPlanSampleStatusEnum status;
}
