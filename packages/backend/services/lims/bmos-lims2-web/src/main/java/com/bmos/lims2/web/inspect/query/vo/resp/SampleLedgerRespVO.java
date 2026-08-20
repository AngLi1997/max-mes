package com.bmos.lims2.web.inspect.query.vo.resp;

import com.bmos.lims2.server.inspect.sample.ledger.enums.SampleLedgerOperationTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 样品台账记录-返回VO
 * @Author: yigaohui
 * @Date: 2025/09/11 11:40
 */
@Getter
@Setter
@ApiModel("样品台账记录-返回VO")
public class SampleLedgerRespVO {

    @ApiModelProperty("台账记录ID")
    private Long id;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单号")
    private String inspectionOrderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("样品ID")
    private Long sampleId;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("操作类型：取样/接收/分样/领取/回收/处理/作废")
    private SampleLedgerOperationTypeEnum operationType;

    @ApiModelProperty("操作时间，格式：yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationTime;

    @ApiModelProperty("操作人ID")
    private String operatorId;

    @ApiModelProperty("操作人名称")
    private String operatorName;

    @ApiModelProperty("备注")
    private String remark;

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
}


