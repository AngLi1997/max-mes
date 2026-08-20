package com.bmos.platform.service.factory.controller.vo;

import com.bmos.common.tree.TreeNode;
import com.bmos.platform.service.equipment.controller.vo.CategoryTreeNodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @className: TenementTreeNode
 * @author: yigaohui
 * @date: 2024/12/30 11:53
 * @Version: 1.0
 * @description:
 */

@Data
@ApiModel("楼宇分类节点")
public class TenementTreeNodeVO implements TreeNode<TenementTreeNodeVO, Long, String> {
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


    @ApiModelProperty("当前分类类型的孩子节点")
    private List<TenementTreeNodeVO> children;

    @Override
    public String sort() {
        return code;
    }
}
