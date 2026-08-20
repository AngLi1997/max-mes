package com.bmos.mes.service.storage.manage.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 暂存物料批次
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/19 15:48
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_storage_material_batch")
@Data
public class StorageMaterialBatch extends BaseDO {

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 原始编码
     */
    private String originalBatchNo;

    /**
     * 有效日期
     */
    private LocalDate expiredDate;

    /**
     * 临期提醒标志
     */
    private Boolean expireWarningFlag;

    /**
     * 可用性
     */
    private Boolean available;

    /**
     * 来源/去向
     */
    private String linkExplain;

    /**
     * 递交人id
     */
    private String senderId;

    /**
     * 接收人id
     */
    private String receiverId;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 扩展单位id
     */
    private Long unitExtendId;

    /**
     * 原厂批号
     */
    private String factoryBatchNo;

    /**
     * 水分(%)
     */
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    private BigDecimal noHydrationContent;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 生产商
     */
    private String producer;

    /**
     * 生产日期
     */
    private LocalDate produceDate;

    /**
     * 报告单编号
     */
    private String reportNo;

    /**
     * 放行单编号
     */
    private String licenceNo;

    /**
     * 物料批次质量状态
     */
    private String qualityStatus;


    /**
     * 最终暴露的单位(有扩展单位优先显示扩展单位 否则显示标准单位)
     *
     * @return
     */
    @JsonIgnore
    public Long getFinalUnitId() {
        return unitExtendId == null ? unitId : unitExtendId;
    }

    /**
     * 单位是否为扩展单位
     *
     * @return
     */
    @JsonIgnore
    public boolean unitIsExtend() {
        return unitExtendId != null;
    }

    public void availableValidate() {
        if (!getAvailable()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_EXPIRED);
        }
        MaterialQualityStatusEnum qualityStatusEnum = MaterialQualityStatusEnum.getEnumByValue(qualityStatus);
        if (!Objects.equals(qualityStatusEnum, MaterialQualityStatusEnum.QUALIFIED) &&
                !Objects.equals(qualityStatusEnum, MaterialQualityStatusEnum.RESTRICTED_RELEASE)) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_BATCH_CANT_USE, qualityStatusEnum.getName());
        }
    }
}
