package com.bmos.wms.service.inventory.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

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
public class InventoryPageQueryWithCargoId extends BasePage {

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id", example = "1")
    private Long cargoCategoryId;

    /**
     * 货品id
     */
    @ApiModelProperty(value = "货品id")
    private Long cargoId;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "氯化钠")
    @Length(max = 100)
    private String cargoName;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "WH03")
    @Length(max = 100)
    private String mergeCode;
}
