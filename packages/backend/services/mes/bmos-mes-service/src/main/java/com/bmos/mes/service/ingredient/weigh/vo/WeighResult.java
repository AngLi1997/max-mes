package com.bmos.mes.service.ingredient.weigh.vo;

import com.bmos.mes.common.enums.ingredient.WeighProcess;
import com.bmos.unit.annotation.PrecisionValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/17 18:04
 */
@Data
@ApiModel("称量打码结果")
public class WeighResult {

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "123456")
    private String no;

    /**
     * 物料总量
     */
    @ApiModelProperty(value = "物料总量", example = "1.00")
    private BigDecimal quantity;

    /**
     * 目标量
     */
    @ApiModelProperty(value = "目标量", example = "1.00")
    private BigDecimal targetQuantity;

    /**
     * 未称量
     */
    @ApiModelProperty(value = "未称量", example = "1.00")
    private BigDecimal unWeighedQuantity;

    /**
     * 已称量
     */
    @ApiModelProperty(value = "已称量", example = "1.00")
    private BigDecimal weighedQuantity;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", example = "kg")
    private String unit;

    /**
     * 下次称量类型
     */
    @ApiModelProperty(value = "下次称量类型", example = "1")
    private WeighProcess nextProcess;

    /**
     * 称量结果
     */
    @ApiModelProperty(value = "称量结果")
    private List<WeighResultItem> resultItemList = new ArrayList();

    @Data
    @ApiModel("称量结果项")
    public static class WeighResultItem {

        /**
         * 是否按件称量
         */
        @ApiModelProperty(value = "是否按件称量", example = "true")
        private Boolean byPiece;

        /**
         * 物料件id
         */
        @ApiModelProperty(value = "物料件id", example = "1")
        private Long storageMaterialId;

        /**
         * 物料件号
         */
        @ApiModelProperty(value = "物料件号", example = "123456")
        private String storageMaterialNo;
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
         * 物料量
         */
        @ApiModelProperty(value = "物料量", example = "1.00")
        @DecimalMin("0.000000001")
        @DecimalMax("9999999999.999999999")
        @PrecisionValue
        private BigDecimal quantity;

        /**
         * 单位
         */
        @ApiModelProperty(value = "单位", example = "kg")
        private String unit;

        /**
         * 容器名称
         */
        @ApiModelProperty(value = "容器名称", example = "1")
        private String containerName;

        /**
         * 货位名称
         */
        @ApiModelProperty(value = "货位名称", example = "1")
        private String materialPositionName;
    }
}
