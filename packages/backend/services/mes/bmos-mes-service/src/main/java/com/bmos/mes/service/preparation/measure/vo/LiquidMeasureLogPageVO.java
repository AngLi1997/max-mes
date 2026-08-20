package com.bmos.mes.service.preparation.measure.vo;

import com.bmos.mes.common.enums.preparation.MeasureTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel("量取日志分页VO")
@Data
public class LiquidMeasureLogPageVO {

    @ApiModelProperty("操作类型")
    private MeasureTypeEnum measureType;

    @ApiModelProperty("量取量")
    private BigDecimal measureQuantity;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("量取人登录账号")
    private String measurerLoginName;

    @ApiModelProperty("量取人名称")
    private String measurerName;

    @ApiModelProperty("复核人登录账号")
    private String reCheckerLoginName;

    @ApiModelProperty("复核人名称")
    private String reCheckerName;

    @ApiModelProperty("物料件号")
    private String materialNo;

    @ApiModelProperty("量取时间")
    private LocalDateTime measureTime;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("生产批号")
    private String productBatchNo;

}
