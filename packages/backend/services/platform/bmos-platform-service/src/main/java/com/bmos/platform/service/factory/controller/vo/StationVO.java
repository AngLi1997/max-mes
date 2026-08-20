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
public class StationVO {

    /**
     * 工位id
     */
    @ApiModelProperty("工位id")
    private Long id;

    /**
     * 工位名称
     */
    @ApiModelProperty("工位名称")
    private String name;

    /**
     * 工位所属模型名称
     */
    @ApiModelProperty("工位所属模型名称")
    private String moduleName;

    @ApiModelProperty("工位所属模型编码")
    private String moduleCode;

    /**
     * 工位描述
     */
    @ApiModelProperty("工位描述")
    private String description;

    /**
     * 工位启停状态
     */
    @ApiModelProperty("工位启停状态")
    private Boolean enable;

    @ApiModelProperty("工位编码")
    private String code;

    @ApiModelProperty("设备详情")
    private List<String> equipmentDetail;

    @ApiModelProperty("人员详情")
    private List<String> userDetail;

    /**
     * 所属产线名称
     */
    private List<String> lineName;

    /**
     * 所属房间名称
     */
    private String roomName;

}
