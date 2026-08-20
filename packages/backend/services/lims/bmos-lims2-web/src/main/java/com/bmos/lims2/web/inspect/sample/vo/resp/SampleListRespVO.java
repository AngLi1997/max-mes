package com.bmos.lims2.web.inspect.sample.vo.resp;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: 样品列表响应VO（与扫码接口保持一致的字段）
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Getter
@Setter
@ApiModel("样品列表响应")
public class SampleListRespVO {

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

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("样品数量")
    private String quantity;

    @ApiModelProperty("单位ID")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("计划取样量")
    private String planQuantity;

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


    public String getInspectionRequestByName() {
        return UserUtils.getUserDisplayName(inspectionRequestBy);
    }

    @ApiModelProperty("标签是否已打印")
    private Boolean tagPrinted;
}
