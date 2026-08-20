package com.bmos.wms.service.inventory.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 货品批次分页查询参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/3 11:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("货品批次分页查询参数")
public class InventoryBatchPageQueryWithCargoId extends BasePage {

    /**
     * 货品批号
     */
    @ApiModelProperty(value = "货品批号", example = "WH030102231001")
    @Length(max = 100)
    private String inventoryBatchNo;

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id", example = "1")
    @NotNull
    private Long cargoId;
}
