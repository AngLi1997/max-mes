package com.bmos.mes.service.weigh.centre2.ticket.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/5/23 18:12
 */
@Data
@ApiModel(description = "称量物料记录VO")
public class TicketWeighMaterialRecordVO {

    @ApiModelProperty(value = "存储物料ID", example = "123456")
    private Long storageMaterialId;

    @ApiModelProperty(value = "存储物料编号", example = "SM20250523001")
    private String storageMaterialNo;

    /**
     * 皮重
     */
    @ApiModelProperty(value = "皮重", example = "1.00")
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    @ApiModelProperty(value = "毛重", example = "1.00")
    private BigDecimal grossWeight;

    /**
     * 净重
     */
    @ApiModelProperty(value = "净重", example = "1.00")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "单位ID", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位名称", example = "千克")
    private String unit;

    @ApiModelProperty(value = "称量人ID", example = "user001")
    private String weigherId;

    @ApiModelProperty(value = "称量人姓名", example = "张三")
    private String weigherName;

    @ApiModelProperty(value = "复核人ID", example = "user002")
    private String recheckerId;

    @ApiModelProperty(value = "复核人姓名", example = "李四")
    private String recheckerName;

    @ApiModelProperty(value = "称量时间", example = "2025-05-23 18:30:00")
    private LocalDateTime weighTime;
}
