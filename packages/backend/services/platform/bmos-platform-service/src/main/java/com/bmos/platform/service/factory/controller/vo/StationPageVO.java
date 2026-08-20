package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 工位分页返回值
 */
@Getter
@Setter
@ApiModel("工位分页返回值")
public class StationPageVO {

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

    @ApiModelProperty("工位编码")
    private String code;

    /**
     * 工位所属模型名称
     */
    @ApiModelProperty("工位所属模型名称")
    private String moduleName;

    @ApiModelProperty("工位所属模型id")
    private Long moduleId;

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

    @ApiModelProperty("设备id集合")
    private List<Long> equipmentIdList;

    @ApiModelProperty("用户id集合")
    private List<String> userIdList;

}
