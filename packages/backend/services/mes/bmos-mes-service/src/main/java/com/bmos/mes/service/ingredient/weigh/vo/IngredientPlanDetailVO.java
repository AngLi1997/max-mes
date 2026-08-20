package com.bmos.mes.service.ingredient.weigh.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.IngredientWeighStatus;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 配料单详情vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/17 17:07
 */
@Data
@ApiModel("配料单详情vo")
public class IngredientPlanDetailVO {

    /**
     * 配料单id
     */
    @ApiModelProperty(value = "配料单id", example = "1")
    private Long id;

    /**
     * 配料单名称
     */
    @ApiModelProperty(value = "配料单名称", example = "人血白蛋白-2402016-01")
    private String name;

    /**
     * 批次列表
     */
    @ApiModelProperty(value = "批次列表")
    @Valid
    private List<IngredientPlanDetailBatchVO> batchList = new ArrayList<>();

    @Data
    @ApiModel("配料单批次信息")
    public static final class IngredientPlanDetailBatchVO {

        /**
         * 物料id
         */
        @ApiModelProperty(value = "物料id", example = "1")
        private Long materialId;

        /**
         * 物料名称
         */
        @ApiModelProperty(value = "物料名称", example = "盐酸")
        private String materialName;

        /**
         * 物料编码
         */
        @ApiModelProperty(value = "物料编码", example = "001")
        private String materialCode;

        /**
         * 物料合并编码
         */
        @ApiModelProperty(value = "物料合并编码", example = "WH03-001")
        private String mergeCode;

        /**
         * 暂存物料批次id
         */
        @ApiModelProperty(value = "暂存物料批次id", example = "1")
        private Long storageMaterialBatchId;

        /**
         * 暂存物料批次编号
         */
        @ApiModelProperty(value = "暂存物料批次编号", example = "YH101001-231001")
        private String storageMaterialBatchNo;

        /**
         * 目标配料量
         */
        @ApiModelProperty(value = "目标配料量", example = "100")
        private BigDecimal targetQuantity;

        /**
         * 已称量配料量
         */
        @ApiModelProperty(value = "已称量配料量", example = "100")
        private BigDecimal finishedQuantity;

        /**
         * 未称量配料量
         */
        @ApiModelProperty(value = "未称量配料量", example = "100")
        private BigDecimal unFinishedQuantity;

        /**
         * 单位id
         */
        @ApiModelProperty(value = "单位id", example = "1")
        private Long unitId;

        /**
         * 单位
         */
        @ApiModelProperty(value = "单位", example = "kg")
        private String unit;

        /**
         * 称量状态
         */
        @ApiModelEnumProperty(value = "称量状态", enumClass = IngredientWeighStatus.class)
        @EnumValidate(IngredientWeighStatus.class)
        private IngredientWeighStatus weighStatus;
    }
}
