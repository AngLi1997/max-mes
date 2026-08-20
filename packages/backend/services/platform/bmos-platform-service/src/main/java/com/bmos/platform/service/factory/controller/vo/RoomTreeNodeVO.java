package com.bmos.platform.service.factory.controller.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("房间树")
public class RoomTreeNodeVO implements TreeNode<RoomTreeNodeVO, Long, String> {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 父节点
     */
    @ApiModelProperty("父节点")
    private Long parentId;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 子节点
     */
    @ApiModelProperty("子节点")
    private List<RoomTreeNodeVO> children;

    /**
     * 房间信息
     */
    @ApiModelProperty("房间信息")
    private List<RoomEasyInfoVO> infoList;

    @Override
    public String sort() {
        return code;
    }
}
