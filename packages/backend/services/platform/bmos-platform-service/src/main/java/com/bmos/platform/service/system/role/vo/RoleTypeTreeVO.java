package com.bmos.platform.service.system.role.vo;

import com.bmos.common.tree.TreeNode;
import com.bmos.common.tree.TreeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApiModel("角色类型树全量VO")
@Getter
@Setter
@ToString
public class RoleTypeTreeVO implements TreeNode<RoleTypeTreeVO,Long, LocalDateTime> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色类型名称")
    private String roleTypeName;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("父级角色类型名称")
    private String parentName;

    @ApiModelProperty("是否有子结构")
    private Boolean flag;

    private LocalDateTime createTime;

    @ApiModelProperty("子数据")
    private List<RoleTypeTreeVO> children;

    @ApiModelProperty("角色数据集合")
    private List<RoleTypeTreeItemVO> roleList;

    @Override
    public LocalDateTime sort() {
        return createTime;
    }

    public List<RoleTypeTreeVO> getChildren() {
        if (children == null){
            children = new ArrayList<>();
        }
        return children;
    }
}
