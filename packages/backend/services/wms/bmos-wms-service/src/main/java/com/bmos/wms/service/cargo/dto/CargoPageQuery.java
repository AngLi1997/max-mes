package com.bmos.wms.service.cargo.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 货品分类dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 19:28
 */
@Data
@ApiModel("分页查询货品参数")
@EqualsAndHashCode(callSuper = true)
public class CargoPageQuery extends BasePage {

    /**
     * 货品名称
     */
    @ApiModelProperty(value = "货品名称", example = "苹果")
    private String cargoName;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "123456")
    private String mergeCode;

    /**
     * 启停
     */
    @ApiModelProperty(value = "启停", example = "true")
    private Boolean enable;

    /**
     * 货品分类id
     */
    @ApiModelProperty(value = "货品分类id", example = "1")
    private Long cargoCategoryId = 0L;
}
