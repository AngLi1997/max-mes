package com.bmos.mes.service.dataset.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 数据集分类创建DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 10:46
 */
@ApiModel("数据集分类创建DTO")
@Data
public class DatasetCategoryCreateDTO {

    @ApiModelProperty(value = "父级id", example = "1")
    private Long parentId;

    @ApiModelProperty(value = "数据集分类名称", example = "数据集分类")
    @NotBlank
    @Length(max = 100)
    private String name;
}
