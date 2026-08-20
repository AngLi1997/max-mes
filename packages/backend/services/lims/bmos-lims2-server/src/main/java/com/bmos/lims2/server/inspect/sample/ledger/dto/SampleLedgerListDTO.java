package com.bmos.lims2.server.inspect.sample.ledger.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @Description: 样品台账列表项
 * @Author: yigaohui
 * @Date: 2025/09/05 11:25
 */
@Data
@ApiModel("样品台账列表项")
public class SampleLedgerListDTO {

    @ApiModelProperty("台账ID")
    private Long id;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单号")
    private String inspectionOrderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("物料规格")
    private String materialSpec;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("样品ID")
    private Long sampleId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("消耗量")
    private String consumption;

    @ApiModelProperty("回收量")
    private String recycleQuantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("样品状态-已取样")
    private Boolean sampled;

    @ApiModelProperty("样品状态-已接收")
    private Boolean received;

    @ApiModelProperty("样品状态-已分样")
    private Boolean divided;

    @ApiModelProperty("样品状态-已领取")
    private Boolean collected;

    @ApiModelProperty("样品状态-已回收")
    private Boolean recycled;

    @ApiModelProperty("样品状态-已处理")
    private Boolean processed;

    @ApiModelProperty("样品状态（0-未取样，1-取样，2-接收，3-分样，4-领取，5-回收，6-处理）")
    private Integer status;

    @ApiModelProperty("操作类型")
    private SampleLedgerOperationTypeEnum operationType;

    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    @ApiModelProperty("操作人ID")
    private Long operatorId;

    @ApiModelProperty("操作人")
    private String operatorName;

    @ApiModelProperty("备注")
    private String remark;
}


