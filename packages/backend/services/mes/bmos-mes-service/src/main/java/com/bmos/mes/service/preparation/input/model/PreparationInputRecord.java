package com.bmos.mes.service.preparation.input.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 配液投入记录表(BmPreparationInputRecord)实体类
 *
 * @author makejava
 * @since 2024-08-01 12:54:47
 */
@Getter
@Setter
@TableName("bm_preparation_input_record")
public class PreparationInputRecord extends BaseDO {

    /**
     * 配料单id
     */
    private Long preparationPlanId;
    /**
     * 暂存物料批次id
     */
    private Long storageMaterialBatchId;
    /**
     * 暂存物料批次编号
     */
    private String storageMaterialBatchNo;
    /**
     * 物料件id
     */
    private Long storageMaterialId;
    /**
     * 物料件编号
     */
    private String storageMaterialNo;
    /**
     * 配方物料id
     */
    private Long formulaMaterialId;
    /**
     * 投料量
     */
    private String quantity;
    /**
     * 投料单位
     */
    private Long unitId;
    /**
     * 设备id
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
     * 投料人id
     */
    private String importerId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 投入记录排序
     */
    private Integer sort;
    /**
     * 投料时间
     */
    private LocalDateTime inputTime;
    /**
     * 配液投入组件实例id
     */
    private Long componentInstanceId;

    /**
     * 签名状态
     */
    private Integer signStatus;

}

