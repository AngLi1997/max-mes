package com.bmos.mes.service.plan.info.vo;

import com.bmos.mes.common.enums.plan.PlanArchiveStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 生产批次简单信息
 */
@Getter
@Setter
@ApiModel("生产批次简单信息VO")
public class PlanEasyInfoVO {

    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private Long id;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("生产工艺版本")
    private String processVersion;

    /**
     * 生产开始时间
     */
    @ApiModelProperty("生产开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("归档状态")
    private PlanArchiveStatusEnum archiveStatus;

    @ApiModelProperty("归档文件Url")
    private String archiveFileUrl;

}
