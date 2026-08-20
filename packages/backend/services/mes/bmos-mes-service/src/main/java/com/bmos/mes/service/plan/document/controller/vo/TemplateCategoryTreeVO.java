package com.bmos.mes.service.plan.document.controller.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 模板分类树形结构
 */
@Getter
@Setter
@ApiModel("模板分类树形结构")
public class TemplateCategoryTreeVO implements TreeNode<TemplateCategoryTreeVO, Long, LocalDateTime> {

    /**
     * 分类id
     */
    @ApiModelProperty("分类id")
    private Long id;

    /**
     * 父级分类id
     */
    @ApiModelProperty("父级分类id")
    private Long parentId;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 分类创建时间
     */
    @ApiModelProperty("分类创建时间")
    private LocalDateTime createTime;

    /**
     * 子分类
     */
    @ApiModelProperty("子分类")
    private List<TemplateCategoryTreeVO> children;

    @Override
    public LocalDateTime sort() {
        return createTime;
    }
}
