package com.bmos.mes.service.weigh.centre2.execute.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.common.enums.weigh.centre2.SignStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.WeighFuncEnum;
import com.bmos.mes.common.enums.weigh.centre2.WeighTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 称量需求的称量记录DO
 */
@Getter
@Setter
@ToString
@TableName("bm_weigh_requirement_record")
public class WeighRequirementRecordDO extends BaseDO {
    /** 称量工单需求id bm_weigh_ticket_requirement_id主键id */
    private Long weighTicketRequirementId;
    /**  称量工单id bm_weigh_ticket主键id */
    private Long ticketId;
    /** 净重 */
    private BigDecimal netWeight;
    /** 皮重 */
    private BigDecimal tareWeight;
    /** 毛重 */
    private BigDecimal grossWeight;
    /** 称量方式 1-手动称量 2-物料称量 */
    private WeighFuncEnum weighFunc;
    /** 称量类型 1-余料称量 2-正常称量 */
    private WeighTypeEnum weighType;
    /** 签名状态 1-已签名 2-未签名 */
    private SignStatusEnum signStatus;
    /** 签名人user_id */
    private String signUser;
    /** 签名时间 */
    private LocalDateTime signTime;
    /** 签名备注 */
    private String signRemark;
    /** 单位id */
    private Long unitId;
    /** 称量人user_id */
    private String weighUserId;
    /** 称量时间 */
    private LocalDateTime weighTime;
    /** 容器id */
    private Long deviceId;
    /** 容器名称 */
    private String deviceName;
    /** 容器编码 */
    private String deviceCode;
    /** 暂存货位id */
    private Long storageId;
    /** 生成的物料件id */
    private Long storageMaterialId;
    /** 物料件号 */
    private String storageMaterialNo;
    /** 物料批次id */
    private Long storageMaterialBatchId;
    /** 物料批号 */
    private String storageMaterialBatchNo;
    /** 生产产品物料id */
    private Long productMaterialId;
}