package com.bmos.mes.service.lotrelease.template.vo;

import com.bmos.mes.service.weigh.centre.config.util.BmosTreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 批签发模板分类vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 10:52
 */
@Data
@ApiModel("批签发模板分类vo")
public class LotReleaseTemplateCategoryVO implements BmosTreeNode<LotReleaseTemplateCategoryVO, Long, Long> {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id", example = "1")
    private Long parentId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", example = "分类名称")
    private String name;

    /**
     * children
     */
    @ApiModelProperty(value = "下级列表")
    private List<LotReleaseTemplateCategoryVO> children;


    @Override
    public void addChild(BmosTreeNode<LotReleaseTemplateCategoryVO, Long, Long> child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add((LotReleaseTemplateCategoryVO) child);
    }

    @Override
    public Long getSortBy() {
        return id;
    }
}