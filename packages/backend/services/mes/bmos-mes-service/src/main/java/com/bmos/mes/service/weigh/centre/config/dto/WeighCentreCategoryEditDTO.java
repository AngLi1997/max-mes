package com.bmos.mes.service.weigh.centre.config.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 更新称量中心分类DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:24
 */
@Data
@ApiModel("更新称量中心分类DTO")
public class WeighCentreCategoryEditDTO {

    /**
     * id
     */
    @NotNull
    private Long id;

    /**
     * 分类名称
     */
    @NotBlank
    @Length(max = 100)
    private String name;
}
