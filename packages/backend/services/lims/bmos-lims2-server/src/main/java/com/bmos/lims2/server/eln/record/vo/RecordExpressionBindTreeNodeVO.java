package com.bmos.lims2.server.eln.record.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("公式记录绑定树节点VO")
@Data
public class RecordExpressionBindTreeNodeVO implements TreeNode<RecordExpressionBindTreeNodeVO, Long, Long> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点id")
    private Long parentId;

    @ApiModelProperty("子集")
    private List<RecordExpressionBindTreeNodeVO> children;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("分类标识")
    private Boolean categoryFlag;

    @ApiModelProperty("是否已绑定")
    private Boolean bound;

    @Override
    public Long sort() {
        return id;
    }
}
