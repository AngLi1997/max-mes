package com.bmos.wms.service.cargo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货品分页vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 19:36
 */
@Data
@ApiModel("货品分页vo")
public class CargoPageVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "苹果")
    private String cargoName;

    /**
     * 货品编码
     */
    @ApiModelProperty(value = "货品编码", example = "123456")
    private String cargoCode;

    /**
     * 货品合并编码
     */
    @ApiModelProperty(value = "货品合并编码", example = "123456")
    private String mergeCode;

    /**
     * 规格
     */
    @ApiModelProperty(value = "规格", example = "1kg/袋")
    private String specification;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称", example = "g")
    private String unit;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 货品分类名称
     */
    @ApiModelProperty(value = "货品分类名称", example = "水果")
    private String cargoCategoryName;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean enable;
}
