package com.bmos.mes.service.facotry.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("房间VO")
public class FactoryRoomVO {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 房间标志
     */
    @ApiModelProperty("房间标志")
    private boolean roomFlag;

    @ApiModelProperty("是否删除数据")
    private Boolean disabled;

    /**
     * 展示名称
     */
    @ApiModelProperty("展示名称")
    private String showName;

    /**
     * 产线id,房间id
     */
    @ApiModelProperty("产线id,房间id")
    private String roomIdPath;

    /**
     * 孩子节点
     */
    @ApiModelProperty("孩子节点")
    private List<FactoryRoomVO> children;

}
