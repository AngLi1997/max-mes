package com.bmos.mes.service.process.vo;

import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 工序下绑定的房间信息VO
 */
@Getter
@Setter
@ApiModel("工序下绑定的房间信息VO（产线树）")
public class ProcedureModelRoomVO {


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

    @ApiModelProperty("idPath")
    private String roomIdPath;

    /**
     * 房间状态
     */
    @ApiModelProperty("房间状态")
    private RoomStatusEnum status;

    @ApiModelProperty("房间标识")
    private boolean roomFlag;

    /**
     * 前端展示名称
     */
    @ApiModelProperty("前端展示名称")
    private String showName;

    @ApiModelProperty("是否是删除数据,前端使用")
    private Boolean disabled;

    /**
     * 子集
     */
    private List<ProcedureModelRoomVO> children;

}
