package com.bmos.mes.service.weigh.centre2.requirement.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 配料信息物料查询返回VO
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:16
 */
@Data
@ApiModel(value = "配料信息物料查询返回VO")
public class TicketRequirementVO {

    @ApiModelProperty(value = "物料ID")
    private Long materialId;

    @ApiModelProperty(value = "批次ID")
    private Long storageMaterialBatchId;

    @ApiModelProperty(value = "物料批号")
    private String materialBatchNo;

    @ApiModelProperty(value = "水分(%)")
    private BigDecimal hydration;

    @ApiModelProperty(value = "无水含量(%)")
    private BigDecimal noHydrationContent;

    @ApiModelProperty(value = "库存量")
    private BigDecimal quantity;

    @ApiModelProperty(value = "已占用")
    private BigDecimal occupancyQuantity;

    @ApiModelProperty("理论量")
    private BigDecimal theoreticalQuantity;

    @ApiModelProperty(value = "单位ID")
    private Long unitId;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "有效期至")
    private LocalDate expiredDate;

    @ApiModelProperty(value = "供应商")
    private String supplier;

    @ApiModelProperty(value = "生产商")
    private String producer;

    @ApiModelProperty(value = "原厂批号")
    private String factoryBatchNo;

    @ApiModelProperty(value = "原始编码")
    private String originalBatchNo;

    @ApiModelProperty(value = "报告单号")
    private String reportNo;

    @ApiModelProperty(value = "放行单号")
    private String licenceNo;
} 