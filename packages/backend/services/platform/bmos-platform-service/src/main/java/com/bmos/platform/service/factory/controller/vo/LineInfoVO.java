package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 产线信息
 */
@Getter
@Setter
@ApiModel("产线信息")
public class LineInfoVO {

    /**
     * 产线id
     */
    @ApiModelProperty("产线id")
    private Long id;

    /**
     * 模型id
     */
    @ApiModelProperty("模型名称")
    private String moduleName;


    @ApiModelProperty("模型编码")
    private String moduleCode;

    /**
     * 产线编码
     */
    @ApiModelProperty("产线编码")
    private String  code;

    /**
     * 产线名称
     */
    @ApiModelProperty("产线名称")
    private String name;

    /**
     * 产线描述
     */
    @ApiModelProperty("产线描述")
    private String description;

    /**
     * 工位名称列表
     */
    @ApiModelProperty("工位名称列表")
    private List<CodeNameVO> stationNameList;

    /**
     * 房间名称列表
     */
    @ApiModelProperty("房间名称列表")
    private List<CodeNameVO> roomNameList;

}
