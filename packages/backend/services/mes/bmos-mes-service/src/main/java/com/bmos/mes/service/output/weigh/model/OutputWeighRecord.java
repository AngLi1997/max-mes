package com.bmos.mes.service.output.weigh.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产出称量记录
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/28 09:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_output_weigh_record")
public class OutputWeighRecord extends BaseDO {

    /**
     * 产出称量流程id
     */
    private Long outputWeighProcessId;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件号
     */
    private String storageMaterialNo;

    /**
     * 暂存物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 是否按件称量
     */
    private Boolean byPiece;

    /**
     * 皮重
     */
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    private BigDecimal grossWeight;

    /**
     * 净重
     */
    private BigDecimal netWeight;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 容器id
     */
    private Long containerId;

    /**
     * 容器名称
     */
    private String containerName;

    /**
     * 货位id
     */
    private Long materialPositionId;

    /**
     * 称量模式
     */
    private WeighMode weighMode;

    /**
     * 签名状态
     */
    private WeighSignStatus signStatus;

    /**
     * 称量人id
     */
    private String weigherId;

    /**
     * 复核人id
     */
    private String reCheckerId;

    /**
     * 称量时间
     */
    private LocalDateTime weighTime;
}
