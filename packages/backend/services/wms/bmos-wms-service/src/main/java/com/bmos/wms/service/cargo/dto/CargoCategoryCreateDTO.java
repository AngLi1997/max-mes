package com.bmos.wms.service.cargo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 货品分类dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 19:28
 */
@Data
@ApiModel("新增货品分类dto")
public class CargoCategoryCreateDTO {

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id", example = "1")
    private Long parentId = 0L;

    /**
     * 货位分类名称
     */
    @ApiModelProperty(value = "货位分类名称", example = "氯化钠", required = true)
    @NotBlank
    @Length(max = 100)
    private String cargoCategoryName;

    /**
     * 货位分类编码
     */
    @ApiModelProperty(value = "货位分类编码", example = "WH03", required = true)
    @NotBlank
    @Length(max = 100)
    private String cargoCategoryCode;
}
