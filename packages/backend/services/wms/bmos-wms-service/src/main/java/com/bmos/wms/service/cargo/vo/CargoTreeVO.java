package com.bmos.wms.service.cargo.vo;

import com.bmos.common.tree.TreeNode;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/10 16:31
 */
@Data
public class CargoTreeVO implements TreeNode<CargoTreeVO, Long, String> {

    /**
     * 货品id/货品分类id
     */
    private Long id;

    /**
     * 父级id
     */
    private Long parentId = 0L;

    /**
     * 货品名称/货品分类名称
     */
    private String name;

    /**
     * 合并编码
     */
    private String mergeCode;

    /**
     * 是否是分类信息
     */
    private Boolean isCategory;

    /**
     * 子列表
     */
    private List<CargoTreeVO> children = new ArrayList<>();

    @Override
    public String sort() {
        return this.mergeCode;
    }

    public String getName() {
        return mergeCode + "-" + name;
    }
}
