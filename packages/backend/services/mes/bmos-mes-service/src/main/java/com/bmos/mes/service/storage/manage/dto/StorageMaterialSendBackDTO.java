package com.bmos.mes.service.storage.manage.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 暂存物料批次入库参数(原退库接口)
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("暂存物料批次入库参数(原退库接口)")
public class StorageMaterialSendBackDTO {

    // 物料信息

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1", required = true)
    @NotNull
    private Long materialId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "WH030102231001", required = true)
    @NotBlank
    @Length(max = 100)
    private String materialBatchNo;

    /**
     * 暂存货位id
     */
    @ApiModelProperty(value = "暂存货位id", example = "1", required = true)
    @NotNull
    private Long materialPositionId;

    // 入库信息

    /**
     * 入库信息列表
     */
    @ApiModelProperty(value = "入库信息列表", required = true)
    @NotEmpty
    @Valid
    private List<SendBackDTO> sendBackList;

    /**
     * 来源/去向
     */
    @ApiModelProperty(value = "来源/去向", example = "123", required = true)
    @NotBlank
    @Length(max = 200)
    private String linkExplain;

    /**
     * 递交人id
     */
    @ApiModelProperty(value = "递交人id", example = "1", required = true)
    @NotBlank
    private String senderId;

    /**
     * 接收人id
     */
    @ApiModelProperty(value = "接收人id", example = "1", required = true)
    @NotBlank
    private String receiverId;


    /**
     * 入库信息
     */
    @Data
    @ApiModel("暂存物料批次入库 入库信息")
    public static class SendBackDTO {

        /**
         * 单件量
         */
        @ApiModelProperty(value = "单件量", example = "1.000", required = true)
        @NotNull
        @DecimalMin(value = "0.000000001", message = "单件量必须大于0")
        @DecimalMax(value = "9999999999.999999999", message = "单件量不能大于9999999999.999999999")
        private BigDecimal singleQuantity;

        /**
         * 基本单位id
         */
        @ApiModelProperty(value = "基本单位id", example = "1", required = true)
        @NotNull
        private Long unitId;

        /**
         * 扩展单位id
         */
        @ApiModelProperty(value = "扩展单位id", example = "1", required = true)
        private Long unitExtendId;

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
    }
}
