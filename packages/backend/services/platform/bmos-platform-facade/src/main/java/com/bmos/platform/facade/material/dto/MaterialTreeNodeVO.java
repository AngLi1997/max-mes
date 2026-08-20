package com.bmos.platform.facade.material.dto;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel("物料树节点VO")
@Data
public class MaterialTreeNodeVO implements TreeNode<MaterialTreeNodeVO, Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点")
    private Long parentId;

    @ApiModelProperty("子节点")
    private List<MaterialTreeNodeVO> children;

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

    @ApiModelProperty("是否是分类")
    private Boolean categoryFlag;

    @Override
    public String sort() {
        return getMergeCode();
    }
}
