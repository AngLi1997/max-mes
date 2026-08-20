package com.bmos.wms.service.inventory.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 货品分页查询参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/3 11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("货品分页查询参数")
public class InventoryPageQueryWithBatchId extends BasePage {

    /**
     * 货品批次id
     */
    @ApiModelProperty(value = "货品批次id", example = "1", required = true)
    @NotNull
    private Long inventoryBatchId;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1")
    private Long positionId;

    /**
     * 货品件号
     */
    @ApiModelProperty(value = "货品件号", example = "001")
    @Length(max = 100)
    private String inventoryNo;
}
