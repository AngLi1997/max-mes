package com.bmos.mes.service.plan.document.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 版本管理返回的VO
 */
@Getter
@Setter
@ApiModel("版本管理返回的VO")
public class BatchRecordVersionVO {

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 产品编码
     */
    @ApiModelProperty("产品编码")
    private String productCode;

    /**
     * 产品规格
     */
    @ApiModelProperty("产品规格")
    private String productSpecification;

    /**
     * 工艺id
     */
    @ApiModelProperty("工艺id")
    private Long processId;

    /**
     * 工艺名称
     */
    @ApiModelProperty("工艺名称")
    private String processName;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    /**
     * 生产开始时间
     */
    @ApiModelProperty("生产开始时间")
    private LocalDateTime startTime;

    /**
     * 生产结束时间
     */
    @ApiModelProperty("生产结束时间")
    private LocalDateTime endTime;

    /**
     * 批签发模板名称
     */
    @ApiModelProperty("批签发模板名称")
    private String templateName;

    /**
     * 产品id
     */
    @ApiModelProperty("产品id")
    private Long productId;
}
