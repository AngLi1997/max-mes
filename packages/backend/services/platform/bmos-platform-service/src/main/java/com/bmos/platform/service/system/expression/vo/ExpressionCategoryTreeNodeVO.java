package com.bmos.platform.service.system.expression.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("ExpressionCategoryTreeNodeVO:公式分类树节点VO")
public class ExpressionCategoryTreeNodeVO implements TreeNode<ExpressionCategoryTreeNodeVO, Long, LocalDateTime> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点")
    private Long parentId;

    @ApiModelProperty("子节点")
    private List<ExpressionCategoryTreeNodeVO> children;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @Override
    public LocalDateTime sort() {
        return getCreateTime();
    }
}
