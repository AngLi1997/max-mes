package com.bmos.mes.service.storage.manage.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 暂存物料件
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/19 15:48
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_storage_material")
@Data
public class StorageMaterial extends BaseDO {

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 暂存货位id
     */
    private Long materialPositionId;

    /**
     * 物料件号
     */
    private String no;

    /**
     * 初始量
     */
    private BigDecimal initQuantity;

    /**
     * 可用量
     */
    private BigDecimal availableQuantity;

    /**
     * 消耗量
     */
    private BigDecimal consumeQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 扩展单位id
     */
    private Long unitExtendId;

    /**
     * 预订量
     */
    private BigDecimal reserveQuantity;

    /**
     * 容器id
     */
    private Long containerId;

    /**
     * 容器名称
     */
    private String container;

    /**
     * 签名状态
     */
    private WeighSignStatus signStatus;

    /**
     * 物料来源
     */
    private String source;

    /**
     * 投入使用的生产计划id
     */
    private Long productPlanId;

    /**
     * 产品id （来源是物料称量，和上面的productPlanId不对应是正常的！！！）
     */
    private Long productId;

    /**
     * 生产批号 （来源是物料称量，和上面的productPlanId不一样是正常的！！！）
     */
    private String batchNo;

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

    /**
     * 是否已被预订(预定量>0)
     *
     * @return true:已被预订 false:未被预订
     */
    public boolean isReserved() {
        return reserveQuantity.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 物料量(可用量 + 预订量)
     *
     * @return
     */
    @JsonIgnore
    public BigDecimal getQuantity() {
        return availableQuantity.add(reserveQuantity);
    }

    /**
     * 是否可用
     *
     * @return
     */
    public Boolean isAvailable() {
        if (signStatus == null) {
            return !(BigDecimal.ZERO.equals(availableQuantity) && BigDecimal.ZERO.equals(reserveQuantity));
        } else {
            return Objects.equals(signStatus, WeighSignStatus.SIGNED) && !(BigDecimal.ZERO.equals(availableQuantity) && BigDecimal.ZERO.equals(reserveQuantity));
        }
    }

    public void availableValidate() {
        if (!isAvailable()) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_AVAILABLE);
        }
    }

    public void outboundValidate() {
        if (this.materialPositionId != null) {
            throw new BmosException(MesResponseCode.STORAGE_MATERIAL_NOT_OUTBOUND);
        }
    }

    /**
     * 消耗所有物料量
     */
    public void consumeAllQuantity() {
        this.consumeQuantity = this.consumeQuantity.add(getQuantity());
        this.reserveQuantity = BigDecimal.ZERO;
        this.availableQuantity = BigDecimal.ZERO;
    }

    /**
     * 消耗物料量
     * 物料只会被整件预定
     * 所以直接从可用量和预定量中有值的一个量进行扣减
     * @param quantity
     */
    public void consumeQuantity(BigDecimal quantity) {
        this.consumeQuantity = this.consumeQuantity.add(quantity);
        if (isReserved()) {
            this.reserveQuantity = this.reserveQuantity.subtract(quantity);
        } else {
            this.availableQuantity = this.availableQuantity.subtract(quantity);
        }
    }
}
