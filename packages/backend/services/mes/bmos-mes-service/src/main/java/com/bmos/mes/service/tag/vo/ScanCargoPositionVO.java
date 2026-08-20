package com.bmos.mes.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/18 17:26
 */
@Data
@ApiModel("扫码货位信息")
public class ScanCargoPositionVO {

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1")
    private Long id;

    /**
     * 货位名称
     */
    @ApiModelProperty(value = "货位名称", example = "货位")
    private String name;

    /**
     * 货位编号
     */
    @ApiModelProperty(value = "货位编号", example = "A01")
    private String code;

    /**
     * 货位全称
     */
    @ApiModelProperty(value = "货位全称", example = "A01-货位")
    private String fullName;
}
