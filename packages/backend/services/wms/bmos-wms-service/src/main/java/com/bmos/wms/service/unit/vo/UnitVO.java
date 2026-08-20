package com.bmos.wms.service.unit.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/11 11:45
 */
@ApiModel("单位信息")
@Data
public class UnitVO implements TreeNode<UnitVO, Long, Long> {

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
     * 是否是扩展单位
     */
    @ApiModelProperty(value = "是否是扩展单位", example = "true")
    private Boolean isExtend;

    /**
     * 表达式
     */
    @ApiModelProperty(value = "表达式", example = "1kg=1000g")
    private String expression;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", example = "g")
    private String name;

    /**
     * 扩展单位列表
     */
    @ApiModelProperty(value = "扩展单位列表")
    private List<UnitVO> children = new ArrayList<>();

    @Override
    public Long sort() {
        return id;
    }
}
