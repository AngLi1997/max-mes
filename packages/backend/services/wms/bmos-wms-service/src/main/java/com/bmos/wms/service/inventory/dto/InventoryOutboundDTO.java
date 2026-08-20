package com.bmos.wms.service.inventory.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 货品出库参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("货品出库参数")
public class InventoryOutboundDTO {

    /**
     * 货品批次id
     */
    @ApiModelProperty(value = "货品批次id", example = "1", required = true)
    @NotNull
    private Long inventoryBatchId;

    /**
     * 出库货品列表
     */
    @ApiModelProperty(value = "出库货品列表", required = true)
    @NotEmpty
    private List<OutBoundDTO> inventories;

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
     * 出库信息
     */
    @Data
    @ApiModel("货品批次出库信息")
    public static class OutBoundDTO {

        /**
         * 物料件id
         */
        @ApiModelProperty("货品件id")
        @NotNull
        private Long id;

        /**
         * 出库量
         */
        @ApiModelProperty("出库数量")
        @DecimalMin(value = "0.000000001", message = "出库数量必须大于0")
        @DecimalMax(value = "9999999999.999999999", message = "出库数量不能大于9999999999.999999999")
        @NotNull
        private BigDecimal quantity;
    }
}
