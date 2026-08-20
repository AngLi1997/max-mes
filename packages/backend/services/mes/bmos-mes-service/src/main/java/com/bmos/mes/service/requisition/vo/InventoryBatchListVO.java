package com.bmos.mes.service.requisition.vo;

import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel("仓库批次列表VO")
@Data
public class InventoryBatchListVO {

    @ApiModelProperty("批次id")
    private Long id;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String mergeCode;

    @ApiModelProperty("批号")
    private String materialBatchNo;

    @ApiModelProperty("原厂批号")
    private String factoryBatchNo;

    @ApiModelProperty("生产日期")
    private LocalDate produceDate;

    @ApiModelProperty("有效日期")
    private LocalDate expiredDate;

    @ApiModelProperty("水分(%)")
    private BigDecimal hydration;

    @ApiModelProperty("无水含量(%)")
    private BigDecimal noHydrationContent;

    @ApiModelProperty("单位id")
    @PrecisionUnitId
    private Long unitId;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

    @ApiModelProperty("可用量")
    @PrecisionValue
    private BigDecimal availableQuantity;

    @ApiModelProperty("预定量")
    private BigDecimal reserveQuantity;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("物料规格")
    private String specification;


}
