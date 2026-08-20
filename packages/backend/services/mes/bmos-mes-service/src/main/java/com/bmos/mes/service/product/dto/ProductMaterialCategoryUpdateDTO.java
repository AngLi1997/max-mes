package com.bmos.mes.service.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("生产物料分类更新DTO")
public class ProductMaterialCategoryUpdateDTO {

    @ApiModelProperty(value = "id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "分类名称", required = true)
    @NotBlank
    private String name;

    /**
     * 自定义字段DTO
     */
    @ApiModelProperty("自定义字段DTO")
    private List<MaterialFieldSaveDTO> fieldSaveDTOList;

}
