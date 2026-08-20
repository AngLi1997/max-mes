package com.bmos.platform.facade.factory.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 产线模型
 */
@Getter
@Setter
@ApiModel("产线模型VO")
public class LineModuleTreeNodeFeignVO implements TreeNode<LineModuleTreeNodeFeignVO, Long, String> {

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
    private List<LineModuleTreeNodeFeignVO> children;

    /**
     * 房间信息
     */
    @ApiModelProperty("产线信息")
    private List<FactoryLineFeignVO> infoList;

    @Override
    public String sort() {
        return code;
    }

}
