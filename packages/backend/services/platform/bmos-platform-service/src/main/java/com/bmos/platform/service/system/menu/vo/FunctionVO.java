package com.bmos.platform.service.system.menu.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ApiModel("功能VO")
@Getter
@Setter
@ToString
public class FunctionVO implements TreeNode<FunctionVO,Long,Long> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("功能名称")
    private String name;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("是否被选中")
    private Boolean flag;

    @ApiModelProperty("子集")
    private List<FunctionVO> children;

    @Override
    public Long sort() {
        return id;
    }
}
