package com.bmos.lims2.server.operate.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "分类返回vo")
public class OperateRuleCategoryVO implements TreeNode<OperateRuleCategoryVO, Long, String> {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "下级集合")
    private List<OperateRuleCategoryVO> children;

    @Override
    public String sort() {
        return String.valueOf(id);
    }
}
