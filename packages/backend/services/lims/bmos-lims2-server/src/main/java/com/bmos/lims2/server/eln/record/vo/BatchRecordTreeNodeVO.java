package com.bmos.lims2.server.eln.record.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("记录树VO")
@Data
public class BatchRecordTreeNodeVO implements TreeNode<BatchRecordTreeNodeVO, Long, Long> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("子集")
    private List<BatchRecordTreeNodeVO> children;

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
