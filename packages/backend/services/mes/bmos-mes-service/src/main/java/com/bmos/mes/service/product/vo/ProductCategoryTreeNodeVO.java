package com.bmos.mes.service.product.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("工艺配置产品分类树")
public class ProductCategoryTreeNodeVO implements TreeNode<ProductCategoryTreeNodeVO,Long, String> {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("分类或产品名称")
    private String name;

    @ApiModelProperty("是否是分类")
    private Boolean categoryFlag;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty("展示名称")
    private String showName;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("产品规格")
    private String specification;

    @ApiModelProperty("子集")
    private List<ProductCategoryTreeNodeVO> children;

    private LocalDateTime createTime;


    @Override
    public String sort() {
        return getMergeCode();
    }
}
