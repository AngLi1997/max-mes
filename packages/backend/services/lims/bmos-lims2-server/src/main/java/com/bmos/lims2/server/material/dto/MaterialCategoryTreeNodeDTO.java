package com.bmos.lims2.server.material.dto;

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
@ApiModel("检品分类VO")
public class MaterialCategoryTreeNodeDTO implements TreeNode<MaterialCategoryTreeNodeDTO, Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点")
    private Long parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("展示名称")
    private String showName;

    @ApiModelProperty("是否为分类节点")
    private boolean categoryFlag;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("子节点")
    private List<MaterialCategoryTreeNodeDTO> children;

    @Override
    public String sort() {
        return getMergeCode();
    }
}
