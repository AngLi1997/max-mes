package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 房间分页VO
 */
@Getter
@Setter
@ApiModel("房间分页VO")
public class RoomPageVO {

    /**
     * 房间id
     */
    @ApiModelProperty("房间id")
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
     * 是否启用
     */
    @ApiModelProperty("是否启用")
    private Boolean enable;

    /**
     * 工位id列表
     */
    @ApiModelProperty(value = "工位id列表")
    private List<Long> stationIdList;

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


    /**
     * 楼栋id
     */
    @ApiModelProperty("楼栋id")
    private Long tenementId;


    @ApiModelProperty("楼栋名称")
    private String tenementName;
    /**
     * 楼层id
     */
    @ApiModelProperty("楼层id")
    private Long floorId;

    @ApiModelProperty("楼层名称")
    private String floorName;

    /**
     * 洁净等级
     */
    @ApiModelProperty("洁净等级")
    private String cleanLevel;
    /**
     * 3D模型id
     */
    @ApiModelProperty("3D模型id")
    private String threeDModelId;
}
