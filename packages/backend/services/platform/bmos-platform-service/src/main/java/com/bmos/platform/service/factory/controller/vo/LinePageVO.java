package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 产线分页VO
 */
@Getter
@Setter
@ApiModel("产线分页VO")
public class LinePageVO {

    /**
     * 产线id
     */
    @ApiModelProperty("产线 id")
    private Long id;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 模型id
     */
    @ApiModelProperty("模型id")
    private Long moduleId;

    /**
     * 清洁时限（单位h）
     */
    @ApiModelProperty(value = "清洁时限（单位h）")
    private String timeLimit;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 启停状态
     */
    @ApiModelProperty("启停")
    private Boolean enable;

    /**
     * 工位id列表
     */
    @ApiModelProperty(value = "工位id列表")
    private List<Long> stationIdList;

    /**
     * 房间id列表
     */
    @ApiModelProperty(value = "房间id列表")
    private List<Long> roomIdList;

    /**
     * 最后更新人
     */
    @ApiModelProperty("最后更新人")
    private String operator;

    /**
     * 最后更新时间
     */
    @ApiModelProperty("最后更新时间")
    private String operateTime;
}
