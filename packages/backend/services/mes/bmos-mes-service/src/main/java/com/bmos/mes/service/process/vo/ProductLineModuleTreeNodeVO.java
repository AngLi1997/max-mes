package com.bmos.mes.service.process.vo;

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
public class ProductLineModuleTreeNodeVO implements TreeNode<ProductLineModuleTreeNodeVO, Long, String> {

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
    private List<ProductLineModuleTreeNodeVO> children;

    /**
     * 房间信息
     */
    @ApiModelProperty("产线信息")
    private List<ProductLineVO> infoList;

    @Override
    public String sort() {
        return code;
    }

}
