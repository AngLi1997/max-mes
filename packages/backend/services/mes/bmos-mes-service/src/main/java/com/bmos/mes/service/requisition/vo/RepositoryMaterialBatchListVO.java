package com.bmos.mes.service.requisition.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("仓库物料批次列表VO")
@Data
public class RepositoryMaterialBatchListVO {

    @ApiModelProperty("物料批次id")
    private Long id;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料编码")
    private String mergeCode;

    @ApiModelProperty("批次号")
    private String materialBatchNo;

    @ApiModelProperty("单位id")
    private Long unitId;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("水分(%)")
    private BigDecimal hydration;

    @ApiModelProperty("无水含量(%)")
    private BigDecimal noHydrationContent;

    @ApiModelProperty("供应商")
    private String supplier;

    @ApiModelProperty("生产商")
    private String producer;

    @ApiModelProperty("有效日期")
    private String expiredDate;

    @ApiModelProperty("原厂批号")
    private String originBatchNo;

    @ApiModelProperty("可用量")
    private BigDecimal availableQuantity;

    @ApiModelProperty("预定量")
    private BigDecimal reserveQuantity;

    @ApiModelProperty("计划量")
    private BigDecimal plannedQuantity;

    @ApiModelProperty("是否已预订")
    private Boolean reserved;

    @ApiModelProperty("理论量")
    private BigDecimal theoreticalQuantity;

    @ApiModelProperty("物料批次")
    private String specification;

}
