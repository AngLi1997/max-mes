package com.bmos.mes.service.weigh.centre.config.vo;

import com.bmos.mes.service.weigh.centre.config.util.BmosTreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 称量中心分类vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:21
 */
@Data
@ApiModel("称量中心分类vo")
@NoArgsConstructor
public class WeighCentreCategoryVO implements BmosTreeNode<WeighCentreCategoryVO, Long, Long> {

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
    @ApiModelProperty(value = "分类名称", example = "称量中心分类名称", required = true)
    private String name;

    /**
     * children
     */
    @ApiModelProperty(value = "下级列表")
    private List<WeighCentreCategoryVO> children;

    /**
     * 构造方法
     *
     * @param id       id
     * @param parentId 父级id
     * @param name     分类名称
     */
    public WeighCentreCategoryVO(Long id, Long parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }


    @Override
    public void addChild(BmosTreeNode<WeighCentreCategoryVO, Long, Long> child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add((WeighCentreCategoryVO) child);
    }

    @Override
    public Long getSortBy() {
        return id;
    }
}
