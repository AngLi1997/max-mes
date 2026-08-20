package com.bmos.platform.service.material.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("下发物料分类VO")
public class IssueTreeNodeVO implements TreeNode<IssueTreeNodeVO, Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("是否为分类节点")
    private boolean categoryFlag;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("子集")
    private List<IssueTreeNodeVO> children;

    @Override
    public String sort() {
        return getMergeCode();
    }
}
