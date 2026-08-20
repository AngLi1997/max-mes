package com.bmos.mes.service.weigh.centre2.ticket.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.common.enums.weigh.centre2.TicketStatusEnum;
import com.bmos.unit.service.UnitCache;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/21 16:31
 */
@Data
@ApiModel(value = "工单分页查询返回对象")
public class TicketPageVO {

    @ApiModelProperty(value = "工单ID", example = "1")
    private Long id;

    @ApiModelProperty(value = "工单编号", example = "TK2023120100001")
    private String ticketNo;

    @ApiModelProperty(value = "物料名称", example = "聚乙烯")
    private String materialName;

    @ApiModelProperty(value = "物料合并代码", example = "M1001")
    private String materialMergeCode;

    @ApiModelProperty(value = "物料批次号", example = "BATCH2023120100001")
    private String storageMaterialBatchNo;

    @ApiModelProperty(value = "物料批次ID", example = "1")
    private Long storageMaterialBatchId;

    @ApiModelProperty(value = "物料规格", example = "100g/包")
    private String materialSpecification;

    @ApiModelProperty(value = "称重中心名称", example = "中心A")
    private String weighCentreName;

    @ApiModelProperty(value = "称量中心id", example = "1")
    private Long weighCentreId;

    @ApiModelProperty(value = "需求量", example = "10.5")
    private BigDecimal requirementQuantity;

    @ApiModelProperty(value = "单位ID", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位名称", example = "kg")
    private String unit;

    @ApiModelProperty(value = "计划执行日期", example = "2023-12-01")
    private LocalDate planDate;

    @ApiModelEnumProperty(value = "状态", enumClass = TicketStatusEnum.class)
    private TicketStatusEnum status;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}
