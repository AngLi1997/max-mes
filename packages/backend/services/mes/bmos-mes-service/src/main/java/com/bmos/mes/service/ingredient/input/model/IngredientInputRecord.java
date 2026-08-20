package com.bmos.mes.service.ingredient.input.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配料投入记录
 * @author liang
 * @version 1.0.0
 * @date 2024/4/25 21:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_ingredient_input_record")
public class IngredientInputRecord extends BaseDO {

    /**
     * 配料单id
     */
    private Long ingredientPlanId;

    /**
     * 暂存物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 物料件id
     */
    private Long storageMaterialId;

    /**
     * 物料件编号
     */
    private String storageMaterialNo;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 单位id
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
     * 设备编号
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
     * 投料时间
     */
    private LocalDateTime inputTime;

    /**
     * 组件实例id
     */
    private Long componentInstanceId;
}
