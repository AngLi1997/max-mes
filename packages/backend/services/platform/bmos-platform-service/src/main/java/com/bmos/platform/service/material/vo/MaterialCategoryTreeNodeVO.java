package com.bmos.platform.service.material.vo;

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
@ApiModel("物料分类树节点VO")
public class MaterialCategoryTreeNodeVO implements TreeNode<MaterialCategoryTreeNodeVO, Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点")
    private Long parentId;

    @ApiModelProperty("子节点")
    private List<MaterialCategoryTreeNodeVO> children;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("展示名:编码-名称")
    private String showName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @Override
    public String sort() {
        return getMergeCode();
    }
}
