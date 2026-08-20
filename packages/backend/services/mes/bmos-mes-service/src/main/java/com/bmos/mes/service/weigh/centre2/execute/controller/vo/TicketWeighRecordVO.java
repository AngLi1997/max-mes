package com.bmos.mes.service.weigh.centre2.execute.controller.vo;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.weigh.centre2.SignStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("工单称量记录VO")
public class TicketWeighRecordVO {

    @ApiModelProperty("记录id")
    private Long recordId;

    @ApiModelProperty("签名状态")
    private SignStatusEnum signStatus;

    @ApiModelProperty("物料合并编码")
    private String materialMergeCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料类型")
    private CategoryInfoTypeEnum categoryInfoType;

    @ApiModelProperty("物料批号")
    private String storageMaterialBatchNo;

    @ApiModelProperty("物料件号")
    private String storageMaterialNo;

    @ApiModelProperty("净重")
    private BigDecimal netWeight;

    @ApiModelProperty("皮重")
    private BigDecimal tareWeight;

    @ApiModelProperty("毛重")
    private BigDecimal grossWeight;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("产品物料名称")
    private String productMaterialName;

    @ApiModelProperty("产品物料编码")
    private String productMaterialMergeCode;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("称量时间")
    private LocalDateTime weighTime;

    @ApiModelProperty("称量人id")
    private String weighUserId;

    @ApiModelProperty("称量人姓名")
    private String weighUserName;

    @ApiModelProperty("称量登录名")
    private String weighUserLoginName;

    @ApiModelProperty("复核人id")
    private String signUserId;

    @ApiModelProperty("复核人登录名")
    private String signUserLoginName;

    @ApiModelProperty("复核人")
    private String signUserName;

    @ApiModelProperty("容器名称")
    private String deviceName;

    @ApiModelProperty("容器编码")
    private String deviceCode;

    @ApiModelProperty("货位id")
    private Long storageId;

    @ApiModelProperty("货位编码")
    private String storageCode;

    @ApiModelProperty("货位名称")
    private String storageName;

} 