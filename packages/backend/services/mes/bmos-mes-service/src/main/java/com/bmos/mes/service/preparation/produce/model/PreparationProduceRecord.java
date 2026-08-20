package com.bmos.mes.service.preparation.produce.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 配液产出产出记录表(BmPreparationProduceRecord)实体类
 *
 * @author makejava
 * @since 2024-08-01 12:56:05
 */
@Getter
@Setter
@TableName("bm_preparation_produce_record")
public class PreparationProduceRecord extends BaseDO {

    /**
     * 配液产出产出流程id
     */
    private Long procedureProduceProgressId;
    /**
     * 物料件id
     */
    private Long storageMaterialId;
    /**
     * 物料件号
     */
    private String storageMaterialNo;
    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;
    /**
     * 配液物料批次编号
     */
    private String storageMaterialBatchNo;
    /**
     * 皮重
     */
    private String tareWeight;
    /**
     * 毛重
     */
    private String grossWeight;
    /**
     * 净重
     */
    private String netWeight;
    /**
     * 单位id
     */
    private Long unitId;
    /**
     * 容器id
     */
    private Long containerId;
    /**
     * 容器编号
     */
    private String containerCode;
    /**
     * 容器名称
     */
    private String containerName;
    /**
     * 产出模式 1-配料产出 2-手动产出
     */
    private Integer weighMode;
    /**
     * 签名状态 0-未签名 1-已签名 2-已作废
     */
    private Integer signStatus;
    /**
     * 货位id
     */
    private Long materialPositionId;
    /**
     * 产出人id
     */
    private String producerId;
    /**
     * 复核人id
     */
    private String reCheckerId;
    /**
     * 产出时间
     */
    private LocalDateTime produceTime;

    /**
     * 签名时间
     */
    private LocalDateTime signTime;

    /**
     * 产出排序
     */
    private Integer sort;

}

