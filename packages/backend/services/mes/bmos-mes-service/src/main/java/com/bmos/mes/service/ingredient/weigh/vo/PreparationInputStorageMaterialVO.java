package com.bmos.mes.service.ingredient.weigh.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 配液投入物料件信息VO
 */
@Data
@ApiModel("配液投入物料件信息VO")
public class PreparationInputStorageMaterialVO {

    /**
     * 物料件id
     */
    @ApiModelProperty(value = "物料件id", example = "1")
    private Long id;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "01")
    private String no;

    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号", example = "B1")
    private String materialBatchNo;

    /**
     * 批次id
     */
    @ApiModelProperty(value = "批次id", example = "1")
    private Long materialBatchId;

    /**
     * 物料预定量（经配方物料单位和精度换算）
     */
    @PrecisionValue
    @ApiModelProperty(value = "物料量（经配方物料单位和精度换算）", example = "1.000")
    private BigDecimal quantity;

    /**
     * 单位id
     */
    @PrecisionUnitId
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称", example = "kg")
    private String unit;

    /**
     * 水分(%)
     */
    @ApiModelProperty(value = "水分(%)", example = "1.000")
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    @ApiModelProperty(value = "无水含量(%)", example = "1.000")
    private BigDecimal noHydrationContent;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-04-01")
    private LocalDate expiredDate;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "2310001")
    private String factoryBatchNo;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "供应商")
    private String supplier;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "NaCl")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 皮重
     */
    @ApiModelProperty(value = "皮重", example = "1.00")
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    @PrecisionValue
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    @ApiModelProperty(value = "毛重", example = "1.00")
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    @PrecisionValue
    private BigDecimal grossWeight;

    /**
     * 净重
     */
    @ApiModelProperty(value = "净重", example = "1.00")
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    @PrecisionValue
    private BigDecimal netWeight;

    /**
     * 签名状态
     */
    @ApiModelEnumProperty(value = "是否已签名", enumClass = WeighSignStatus.class)
    @EnumValidate(WeighSignStatus.class)
    private WeighSignStatus signStatus;

    /**
     * 容器名称
     */
    @ApiModelProperty(value = "容器名称", example = "不锈钢盆")
    private String containerName;

    /**
     * 容器id
     */
    @ApiModelProperty(value = "容器id", example = "1")
    private Long containerId;

    /**
     * 货位名称
     */
    @ApiModelProperty(value = "货位名称", example = "1")
    private String materialPositionName;

    /**
     * 物料类型
     */
    @ApiModelEnumProperty(value = "物料类型", enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(CategoryInfoTypeEnum.class)
    private CategoryInfoTypeEnum categoryType;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1")
    private String weigherId;

    /**
     * 称量人名称
     */
    @ApiModelProperty(value = "称量人名称", example = "张三")
    private String weigherName;

    /**
     * 称量人登录名
     */
    @ApiModelProperty(value = "称量人登录名", example = "张三")
    private String weigherLoginName;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    private String reCheckerId;

    /**
     * 复核人名称
     */
    @ApiModelProperty(value = "复核人名称", example = "张三")
    private String reCheckerName;

    /**
     * 称量人登录名
     */
    @ApiModelProperty(value = "称量人登录名", example = "张三")
    private String reCheckerLoginName;

    /**
     * 称重时间
     */
    @ApiModelProperty("称重时间")
    private LocalDateTime weighDate;
}
