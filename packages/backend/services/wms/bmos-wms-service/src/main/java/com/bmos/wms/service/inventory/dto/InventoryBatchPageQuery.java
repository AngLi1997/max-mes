package com.bmos.wms.service.inventory.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

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
public class InventoryBatchPageQuery extends BasePage {

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "培养室-盐酸组氨酸货位")
    @Length(max = 100)
    private String cargoName;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "KQ-PY-101")
    @Length(max = 100)
    private String mergeCode;

    /**
     * 货品批号
     */
    @ApiModelProperty(value = "货品批号", example = "WH030102231001")
    @Length(max = 100)
    private String inventoryBatchNo;

    /**
     * 货位/存储区域id
     */
    @ApiModelProperty(value = "货位/存储区域id(不传查询所有)", example = "1")
    private Long positionId;
}
