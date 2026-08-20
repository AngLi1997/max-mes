package com.bmos.platform.service.system.expression.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("MES记录树节点VO")
@Data
public class MesRecordTreeNodeVO implements TreeNode<MesRecordTreeNodeVO,Long,Long> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点id")
    private Long parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("子集")
    private List<MesRecordTreeNodeVO> children;

    @ApiModelProperty("记录分类标识")
    private Boolean categoryFlag;

    @ApiModelProperty("是否已绑定")
    private Boolean bound;

    @Override
    public Long sort() {
        return id;
    }
}
