package com.bmos.mes.service.storage.manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@ApiModel("物料批次列表VO")
@Getter
@Setter
public class MaterialBatchListVO {

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long id;

    /**
     * 物料批次号
     */
    @ApiModelProperty(value = "物料批次号", example = "WH030102231001")
    private String materialBatchNo;

    /**
     * 原始批号
     */
    @ApiModelProperty(value = "原始批号", example = "123")
    private String originalBatchNo;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2024-02-06")
    private LocalDate expiredDate;

}
