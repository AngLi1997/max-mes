package com.bmos.mes.service.weigh.centre.config.vo;

import com.bmos.mes.service.weigh.centre.config.util.BmosTreeNode;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel("称量中心分类vo(携带称量中心)")
@NoArgsConstructor
public class WeighCentreCategoryWithCentreVO implements BmosTreeNode<WeighCentreCategoryWithCentreVO, Long, Long> {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id", example = "1")
    @JsonIgnore
    private Long parentId;

    /**
     * 称量中心名称/分类名称
     */
    @ApiModelProperty(value = "称量中心名称/分类名称", example = "称量中心名称/分类名称", required = true)
    private String name;

    /**
     * children
     */
    @ApiModelProperty(value = "下级列表")
    private List<WeighCentreCategoryWithCentreVO> children;

    /**
     * 称量中心列表
     */
    @JsonIgnore
    @ApiModelProperty(value = "称量中心列表", hidden = true)
    private List<WeighCentreSimpleVO> centriesList;

    /**
     * 是否是分类
     */
    @ApiModelProperty(value = "是否是分类", example = "true")
    private Boolean isCategory = true;

    @Override
    public void addChild(BmosTreeNode<WeighCentreCategoryWithCentreVO, Long, Long> child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add((WeighCentreCategoryWithCentreVO) child);
    }

    @Override
    public Long getSortBy() {
        return id;
    }
}
