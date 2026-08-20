package com.bmos.platform.facade.factory.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 房间模型
 */
@Getter
@Setter
@ApiModel("房间模型VO")
public class RoomModuleTreeNodeFeignVO implements TreeNode<RoomModuleTreeNodeFeignVO, Long, String> {

    /**
     * id
     */
    private Long id;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 子节点
     */
    private List<RoomModuleTreeNodeFeignVO> children;

    /**
     * 房间信息
     */
    @ApiModelProperty("房间信息")
    private List<RoomEasyInfoFeignVO> infoList;

    @Override
    public String sort() {
        return code;
    }

}
