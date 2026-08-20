package com.bmos.mes.service.weigh.centre.input.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物料投入记录
 * @author liang
 * @version 1.0.0
 * @date 2024/7/18 15:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_weigh_input_record")
public class WeighInputRecord extends BaseDO {

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 配料物料id
     */
    private Long formulaMaterialId;

    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 暂存物料id
     */
    private Long storageMaterialId;

    /**
     * 投料量
     */
    private BigDecimal quantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 投料组件id
     */
    private Long inputComponentInstanceId;

    /**
     * 投料人id
     */
    private String inputUserId;

    /**
     * 投料人名称
     */
    private String inputUserName;

    /**
     * 投料时间
     */
    private LocalDateTime inputTime;

    /**
     * 投料设备id
     */
    private Long deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 称量需求id
     */
    private Long requirementId;

    /**
     * 配料计划id
     */
    private Long productPlanId;
}
