package com.bmos.wms.service.cargo.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/22 19:36
 */
@Data
@ApiModel("货品分类vo")
public class CargoCategoryVO implements TreeNode<CargoCategoryVO, Long, String> {

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
     * 货位分类名称
     */
    @ApiModelProperty(value = "货位分类名称", example = "氯化钠")
    private String cargoCategoryName;

    /**
     * 货位分类编码
     */
    @ApiModelProperty(value = "货位分类编码", example = "WH03")
    private String cargoCategoryCode;

    /**
     * 货位合并编码
     */
    @ApiModelProperty(value = "货位合并编码", example = "WH03")
    private String cargoCategoryMergeCode;

    /**
     * 下级节点
     */
    @ApiModelProperty(value = "下级节点")
    private List<CargoCategoryVO> children = new ArrayList<>();

    @Override
    public String sort() {
        return this.cargoCategoryCode;
    }

    /**
     * 拼接mergeCode和名称
     *
     * @return
     */
    public String getFullName() {
        return this.cargoCategoryMergeCode + "-" + this.cargoCategoryName;
    }
}
