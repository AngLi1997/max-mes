package com.bmos.lims2.web.inspect.pack.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 实验包列表响应VO - 用于下拉选择
 * @author system
 */
@Getter
@Setter
@ApiModel("实验包列表响应VO")
public class InspectPackageListRespVO {

    /**
     * 实验包id
     */
    @ApiModelProperty(value = "实验包id")
    private Long id;

    /**
     * 实验包编码
     */
    @ApiModelProperty(value = "实验包编码") 
    private String code;

    /**
     * 实验包名称
     */
    @ApiModelProperty(value = "实验包名称")
    private String name;
}