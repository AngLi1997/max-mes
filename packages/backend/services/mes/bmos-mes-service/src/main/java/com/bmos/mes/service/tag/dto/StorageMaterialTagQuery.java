package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 物料件扫码查询
 * @author liang
 * @version 1.0.0
 * @date 2024/3/15 10:38
 */
@Data
@ApiModel("物料件扫码查询")
public class StorageMaterialTagQuery {

    /**
     * 暂存物料编号
     */
    @ApiModelProperty(value = "物料件编号", example = "001")
    @NotBlank
    private String no;
}
