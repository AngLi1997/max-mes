package com.bmos.mes.service.product.vo;

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
@ApiModel("生产物料分类树节点VO")
public class ProductMaterialCategoryTreeNodeVO implements TreeNode<ProductMaterialCategoryTreeNodeVO, Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("父节点")
    private Long parentId;

    @ApiModelProperty("子节点")
    private List<ProductMaterialCategoryTreeNodeVO> children;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("展示名称")
    private String showName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @Override
    public String sort() {
        return getMergeCode();
    }
}
