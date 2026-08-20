package com.bmos.lims2.server.inspect.sample.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @Description: 样品保存DTO
 * @Author: yigaohui
 * @Date: 2025/01/29 10:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("样品保存数据对象")
public class SampleSaveDTO {

    @ApiModelProperty("样品ID（编辑时传入）")
    private Long id;

    @ApiModelProperty(value = "检验单ID", required = true)
    @NotNull(message = "检验单ID不能为空")
    private Long inspectionOrderId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("是否已取样")
    private Boolean sampled;

    @ApiModelProperty("是否已接收")
    private Boolean received;

    @ApiModelProperty("是否已分样")
    private Boolean divided;

    @ApiModelProperty("是否已领取")
    private Boolean collected;

    @ApiModelProperty("是否作废")
    private Boolean discarded;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("父样品ID")
    private Long parentSampleId;

    @ApiModelProperty("样品数量")
    @Pattern(regexp = "^(?:0|[1-9]\\d{0,5})(?:\\.\\d{1,5})?$", message = "样品数量最多整数6位，小数5位")
    private String quantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("取样人")
    private String samplerName;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("接收人")
    private String receiverName;

    @ApiModelProperty("接收时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("分样人")
    private String dividerName;

    @ApiModelProperty("分样时间")
    private LocalDateTime divideTime;

    @ApiModelProperty("领取人ID")
    private String collectorId;

    @ApiModelProperty("领取人")
    private String collectorName;

    @ApiModelProperty("领取时间")
    private LocalDateTime collectTime;

    @ApiModelProperty("作废人")
    private String discardedBy;

    @ApiModelProperty("作废时间")
    private LocalDateTime discardedTime;

    @ApiModelProperty("作废原因")
    private String discardedReason;

    @ApiModelProperty("备注")
    private String remark;
}
