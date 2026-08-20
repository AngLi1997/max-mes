package com.bmos.mes.service.weigh.free.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自由称量历史记录
 * @author liang
 * @version 1.0.0
 * @date 2025/2/27 10:56
 */
@TableName("bm_free_weigh_history")
@Data
@EqualsAndHashCode(callSuper = true)
public class FreeWeighHistoryDO extends BaseDO {

    @ApiModelProperty(value = "物料件id", example = "1")
    private Long storageMaterialId;

    @ApiModelProperty(value = "皮重", example = "1.00")
    private BigDecimal tareWeight;

    @ApiModelProperty(value = "毛重", example = "1.00")
    private BigDecimal grossWeight;

    @ApiModelProperty(value = "净重", example = "1.00")
    private BigDecimal netWeight;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "称量人id", example = "1")
    private String weigherId;

    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;

    @ApiModelProperty(value = "容器id", example = "1")
    private Long containerId;

    @ApiModelProperty(value = "容器名称", example = "RQ-容器")
    private String containerName;

    @ApiModelProperty(value = "货位id", example = "1")
    private Long positionId;

    @ApiModelProperty(value = "货位名称", example = "HW-货位")
    private String positionName;

    @ApiModelProperty(value = "称量时间", example = "2025-02-27 10:44:00")
    private LocalDateTime weighTime;

    @ApiModelEnumProperty(value = "称量模式", enumClass = WeighMode.class)
    @EnumValidate(WeighMode.class)
    private WeighMode weighMode;

    @ApiModelProperty(value = "称量设备id", example = "1")
    private Long deviceId;
}
