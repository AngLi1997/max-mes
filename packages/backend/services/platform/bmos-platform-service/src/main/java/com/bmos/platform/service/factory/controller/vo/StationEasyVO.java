package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 工位详情返回值
 */
@Getter
@Setter
@ApiModel("工位详情返回值")
public class StationEasyVO {

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    private Long id;

    /**
     * 工位编码
     */
    @ApiModelProperty("工位编码")
    private String code;

    /**
     * 工位名称
     */
    @ApiModelProperty("工位名称")
    private String name;

}
