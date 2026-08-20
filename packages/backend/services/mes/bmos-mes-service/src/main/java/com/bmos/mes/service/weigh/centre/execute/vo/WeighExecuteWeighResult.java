package com.bmos.mes.service.weigh.centre.execute.vo;

import com.bmos.mes.common.enums.weigh.centre.RequirementWeighProcess;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 16:36
 */
@Data
@ApiModel("物料称量称量打码结果")
public class WeighExecuteWeighResult {

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称", example = "1")
    private String productName;

    /**
     * 产品合并编码
     */
    @ApiModelProperty(value = "产品合并编码", example = "1")
    private String productMergeCode;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "1")
    private String batchNo;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "123456")
    private String no;

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
    private RequirementWeighProcess nextProcess;

    /**
     * 称量结果
     */
    @ApiModelProperty(value = "称量结果")
    private List<WeighResultItem> resultItemList = new ArrayList<>();

    @Data
    @ApiModel("物料称量结果项")
    public static class WeighResultItem {

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
         * 物料批号
         */
        @ApiModelProperty(value = "物料批号", example = "1")
        private String storageMaterialBatchNo;

        /**
         * 皮重
         */
        @ApiModelProperty(value = "皮重", example = "1.00")
        private BigDecimal tareWeight;

        /**
         * 毛重
         */
        @ApiModelProperty(value = "毛重", example = "1.00")
        private BigDecimal grossWeight;

        /**
         * 净重
         */
        @ApiModelProperty(value = "净重", example = "1.00")
        private BigDecimal netWeight;

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
