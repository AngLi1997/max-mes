package com.bmos.mes.service.weigh.centre.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 新增称量中心分类DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:24
 */
@Data
@ApiModel("新增称量中心分类DTO")
public class WeighCentreCategoryCreateDTO {

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id", example = "1")
    private Long parentId;

    /**
     * 分类名称
     */
    @NotBlank
    @Length(max = 100)
    @ApiModelProperty(value = "分类名称", example = "称量中心分类", required = true)
    private String name;
}
