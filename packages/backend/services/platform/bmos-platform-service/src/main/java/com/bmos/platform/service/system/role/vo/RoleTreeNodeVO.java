package com.bmos.platform.service.system.role.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class RoleTreeNodeVO implements TreeNode<RoleTreeNodeVO,Long,LocalDateTime> {


    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点id")
    private Long parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("子节点")
    private List<RoleTreeNodeVO> children;

    @ApiModelProperty("是否是角色类型，true为是")
    private boolean roleTypeFlag;

    @Override
    public LocalDateTime sort() {
        return createTime;
    }
}
