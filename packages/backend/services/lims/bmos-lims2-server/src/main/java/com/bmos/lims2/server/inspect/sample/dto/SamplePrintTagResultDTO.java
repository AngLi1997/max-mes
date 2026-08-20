package com.bmos.lims2.server.inspect.sample.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @Description: 样品扫码结果DTO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("样品扫码结果数据对象")
public class SamplePrintTagResultDTO {

    @ApiModelProperty("样品ID")
    private Long id;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("物料规格")
    private String materialSpec;

    @ApiModelProperty("全检品名称")
    private String fullMaterialName;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("检验项目信息")
    private String inspectItemInfo;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("数量带单位")
    private String quantityWithUnit;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("计划取样量")
    private String planQuantity;

    @ApiModelProperty("计划取样量带单位")
    private String planQuantityWithUnit;

    @ApiModelProperty("请验时间")
    private LocalDateTime inspectionRequestTime;

    @ApiModelProperty("请验人id")
    private String inspectionRequestBy;

    @ApiModelProperty("请验人名称")
    private String inspectionRequestByName;

    @ApiModelProperty("是否已取样")
    private Boolean sampled;

    @ApiModelProperty("是否已接收")
    private Boolean received;

    @ApiModelProperty("取样人")
    private String samplerName;

    @ApiModelProperty("取样时间")
    private LocalDateTime samplingTime;

    @ApiModelProperty("接收人")
    private String receiverName;

    @ApiModelProperty("接收时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty("是否已回收")
    private Boolean recycled;

    @ApiModelProperty("是否已处理")
    private Boolean processed;

    @ApiModelProperty("回收时间")
    private LocalDateTime recycledTime;

    @ApiModelProperty("回收人")
    private String recycledBy;

    @ApiModelProperty("回收余量")
    private String recycleQuantity;

    @ApiModelProperty("回收单位ID")
    private Long recycleUnitId;

    @ApiModelProperty("回收单位名称")
    private String recycleUnitName;

    @ApiModelProperty("回收备注")
    private String recycleRemark;

    @ApiModelProperty("处理时间")
    private LocalDateTime processTime;

    @ApiModelProperty("处理人")
    private String processBy;

    @ApiModelProperty("处理方式")
    private String processMethod;

    @ApiModelProperty("处理备注")
    private String processRemark;

    @ApiModelProperty("检验结束时间")
    private LocalDateTime orderEndTime;

    @ApiModelProperty("处理状态：1-待回收，2-待处理，3-已处理")
    private Integer status;

    @ApiModelProperty("是否完成")
    private Boolean finished;

    @ApiModelProperty("标签是否已打印")
    private Boolean tagPrinted;

    public String getInspectionRequestByName() {
        return UserUtils.getUserDisplayName(inspectionRequestBy);
    }
}
