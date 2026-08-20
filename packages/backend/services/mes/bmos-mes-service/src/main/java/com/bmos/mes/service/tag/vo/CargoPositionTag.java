package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货位标签vo
 * @author liang
 * @version 1.0.0
 * @date 2024/3/15 10:39
 */
@Data
@ApiModel("货位标签vo")
public class CargoPositionTag {

    /**
     * 货位名称
     */
    @ApiModelProperty(value = "货位名称", example = "氯化钠货位")
    private String name;

    /**
     * 货位编码 暂存货位编码
     */
    @ApiModelProperty(value = "货位编码", example = "KQ10-01")
    private String code;

    /**
     * 所属区域 暂存货位的所属区域
     */
    @ApiModelProperty(value = "所属区域", example = "培养室暂存间")
    private String storageName;

    /**
     * 打印日期 标签打印日期 yyyy-MM-dd
     */
    @ApiModelProperty(value = "打印日期", example = "2024-05-09")
    private String printDate;
}
