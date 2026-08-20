package com.bmos.mes.service.dataset.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据集分类修改DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 10:46
 */
@Data
@ApiModel("数据集分类修改DTO")
public class DatasetCategoryEditDTO {

    @ApiModelProperty(value = "id", example = "1")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "数据集分类名称", example = "数据集分类")
    @NotBlank
    @Length(max = 100)
    private String name;
}
