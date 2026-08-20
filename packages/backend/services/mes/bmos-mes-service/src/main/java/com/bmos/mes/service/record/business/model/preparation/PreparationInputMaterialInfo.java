package com.bmos.mes.service.record.business.model.preparation;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.preparation.PrepareInputStatusEnum;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配液投入物料件信息
 */
@Getter
@Setter
public class PreparationInputMaterialInfo {

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String materialMergeCode;

    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;

    /**
     * 物料批号
     */
    private String storageMaterialBatchNo;

    /**
     * 物料件号
     */
    private String storageMaterialNo;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 单位
     */
    private String unit;

    /**
     * 投料状态
     */
    private PrepareInputStatusEnum inputStatus;

    /**
     * 投料人id
     */
    private String importerId;

    /**
     * 投料人姓名
     */
    private String importerName;

    /**
     * 投料人显示名称
     */
    private String importShowName;

    /**
     * 物料规格
     */
    private String specification;

    /**
     * 投料时间
     */
    private LocalDateTime inputTime;

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
     * 是否被详情组件处理
     */
    private Boolean handle;

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 配方物料id
     */
    private Long formulaMaterialId;

}
