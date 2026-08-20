package com.bmos.platform.service.factory.controller.vo;

import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import com.bmos.platform.service.factory.service.dto.RoomEnvPropertyWithAcquitPointDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 房间信息
 */
@Getter
@Setter
@ApiModel("房间信息")
public class RoomInfoVO {

    /**
     * 房间id
     */
    @ApiModelProperty("房间id")
    private Long id;

    /**
     * 模型id
     */
    @ApiModelProperty("模型名称")
    private String moduleName;

    /**
     * 房间编码
     */
    @ApiModelProperty("房间编码")
    private String code;

    /**
     * 房间名称
     */
    @ApiModelProperty("房间名称")
    private String name;

    @ApiModelProperty("房间状态")
    private RoomStatusEnum status;

    /**
     * 房间描述
     */
    @ApiModelProperty("房间描述")
    private String description;

    /**
     * 清洁时限
     */
    @ApiModelProperty("清洁时限(单位h)")
    private String timeLimit;

    /**
     * 房间绑定的工位详情
     */
    @ApiModelProperty("房间绑定的工位详情")
    private List<CodeNameVO> stationDetails;


    @ApiModelProperty("房间环境参数")
    private List<RoomEnvPropertyWithAcquitPointDTO> roomEnvPropertyDTOList;

    @ApiModelProperty("楼层名称")
    private String floorName;

    @ApiModelProperty("楼栋名称")
    private String tenementName;

    @ApiModelProperty("洁净等级")
    private String cleanLevel;

    /**
     * 3D模型id
     */
    @ApiModelProperty("3D模型Id")
    private String threeDModelId;


    @ApiModelProperty("产线列表")
    private List<CodeNameVO> lineInfoList;

}
