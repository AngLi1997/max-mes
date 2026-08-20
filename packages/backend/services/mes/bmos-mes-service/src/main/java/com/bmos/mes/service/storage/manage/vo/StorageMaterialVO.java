package com.bmos.mes.service.storage.manage.vo;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.unit.annotation.PrecisionUnitId;
import com.bmos.unit.annotation.PrecisionValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * 暂存物料件信息
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/6 11:58
 */
@Data
@ApiModel("暂存物料件信息")
public class StorageMaterialVO {

    /**
     * 暂存物料件id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "WH03")
    private String mergeCode;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "WH03")
    private String materialCode;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格", example = "25kg/袋")
    private String materialSpecification;

    /**
     * 物料类别id
     */
    @ApiModelProperty(value = "物料分类id")
    private Long materialCategoryId;

    /**
     * 物料类别名称
     */
    @ApiModelProperty(value = "物料分类名称")
    private String materialCategoryName;

    /**
     * 物料类别类型
     */
    @ApiModelProperty(value = "物料类别类型")
    private CategoryInfoTypeEnum materialCategoryType;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "000000002")
    private String materialNo;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001")
    private String materialBatchNo;

    /**
     * 物料批号id
     */
    @ApiModelProperty(value = "物料批号id", example = "1")
    private Long materialBatchId;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 扩展单位id
     */
    @ApiModelProperty(value = "扩展单位id", example = "1")
    private Long unitExtendId;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 单位转换率
     */
    @ApiModelProperty(value = "单位转换率", example = "1000")
    private String rate;

    /**
     * 初始量
     */
    @ApiModelProperty(value = "初始量", example = "1.000")
    @PrecisionValue
    private String initQuantity;

    /**
     * 消耗量
     */
    @ApiModelProperty(value = "消耗量", example = "1.000")
    @PrecisionValue
    private String consumeQuantity;

    /**
     * 可用量
     */
    @ApiModelProperty(value = "可用量", example = "1.000")
    @PrecisionValue
    private String availableQuantity;

    /**
     * 预定量
     */
    @ApiModelProperty(value = "预定量", example = "1.000")
    @PrecisionValue
    private String reserveQuantity;

    /**
     * 物料量(可用+预定)
     */
    @ApiModelProperty(value = "物料量(可用+预定)", example = "1.000")
    @PrecisionValue
    private String quantity;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-02-06")
    private LocalDate expiredDate;

    /**
     * 暂存货位id
     */
    @ApiModelProperty(value = "暂存货位id", example = "1")
    private Long materialPositionId;

    /**
     *
     * 容器
     */
    @ApiModelProperty(value = "容器", example = "不锈钢盆")
    private String container;

    /**
     * 容器id
     */
    @ApiModelProperty(value = "容器id", example = "01")
    private Long containerId;

    /**
     * 原始编码
     */
    @ApiModelProperty(value = "原始编码", example = "WH050101")
    private String originalBatchNo;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "2310001")
    private String factoryBatchNo;

    /**
     * 预定生产计划id
     */
    @ApiModelProperty(value = "预定生产计划id", example = "1")
    private Long productPlanId;

    /**
     * 签名状态
     */
    @ApiModelProperty(value = "签名状态", example = "1")
    private WeighSignStatus signStatus;

    public String getQuantity() {
        return new BigDecimal(availableQuantity).add(new BigDecimal(reserveQuantity)).toString();
    }

    /**
     * 最终暴露的单位(有扩展单位优先显示扩展单位 否则显示标准单位)
     *
     * @return
     */
    @PrecisionUnitId
    public Long finalUnitId;

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
     * 是否可用
     *
     * @return
     */
    public Boolean isAvailable() {
        if (signStatus == null) {
            return !("0".equals(availableQuantity) && "0".equals(reserveQuantity));
        } else {
            return Objects.equals(signStatus, WeighSignStatus.SIGNED) && !("0".equals(availableQuantity) && "0".equals(reserveQuantity));
        }
    }
}
