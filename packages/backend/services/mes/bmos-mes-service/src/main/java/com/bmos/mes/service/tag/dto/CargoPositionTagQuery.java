package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 货位扫码查询
 * @author liang
 * @version 1.0.0
 * @date 2024/3/15 10:38
 */
@Data
@ApiModel("货位扫码查询")
public class CargoPositionTagQuery {

    /**
     * 货位编号
     */
    @ApiModelProperty(value = "货位编号", example = "001")
    @NotBlank
    @Length(max = 100)
    private String no;
}
